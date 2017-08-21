/**
 * Copyright 2017 Dan Smith
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.upthewaterspout.jpfgradle

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class JpfBuildFunctionalTest extends Specification {
    @Rule final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile
    File sourceFile
    File testFile
    String jpfUrl

    def setup() {
        buildFile = testProjectDir.newFile('build.gradle')
        jpfUrl = getClass().getResource("/jpf-core.zip").toString()
    }

    def "Adding the jpf plugin download and unzips jpf"() {
        given:
        defaultBuildFile()

        when:
        BuildResult result = executeTask('test')

        then:
        result.task(':downloadJpf').outcome == TaskOutcome.SUCCESS
        new File(testProjectDir.getRoot(), ".jpf").exists()
    }

    def "Adding the jpf plugin generates a jpf.properties file with the correct source and classpath"() {
        given:
        defaultBuildFile()
        addSampleSourceFile();
        addJPFTest()

        when:
        BuildResult result = executeTask('generateJpfProperties')

        then:
        result.task(':generateJpfProperties').outcome == TaskOutcome.SUCCESS
        Properties props = getJpfProperties()
        toList(props.get("classpath")) == getExpectedClasspath();
        toList(props.get("sourcepath")) == getExpectedSourcepath();
    }

    def "Project with test using jpf successfully builds and runs"() {
        given:
        buildFileWithJUnit()
        addJPFTest()

        when:
        BuildResult result = executeTask('test')

        then:
        result.task(':test').outcome == TaskOutcome.SUCCESS
    }

    private void addJPFTest() {
        testFile = newProjectFile("src/test/java/MinimalJPFTest.java")
        testFile << """
            import org.junit.Test;

            import gov.nasa.jpf.util.test.TestJPF;
            import gov.nasa.jpf.vm.Verify;

            public class MinimalJPFTest extends TestJPF {

              @Test
              public void test() throws InterruptedException {
                if(verifyNoPropertyViolation()) {
                  Verify.incrementCounter(0);
                }
                assertEquals(1, Verify.getCounter(0));
              }
            }
        """
    }

    private void addSampleSourceFile() {
        sourceFile = newProjectFile("src/main/java/SampleSourceClass.java")
        sourceFile << """
            public class SampleSourceClass {
            }
        """
    }

    private final File newProjectFile(String relativePath) {
        File file = new File(testProjectDir.getRoot().getAbsolutePath() + "/" + relativePath);
        file.getParentFile().mkdirs();
        return file;
    }


    private Properties getJpfProperties() {
        File propertiesFile = new File(testProjectDir.getRoot(), "jpf.properties");
        propertiesFile.exists();
        Properties props = new Properties();
        FileReader reader = new FileReader(propertiesFile);
        try {
            props.load(reader)
        } finally {
            reader.close();
        }
        props
    }

    def toList(final String path) {
        Arrays.asList(path.split(File.pathSeparator)).sort();
    }

    private File defaultBuildFile() {
        buildFile << """
            plugins {
              id 'com.github.upthewaterspout.jpf'
            }

            apply plugin: 'java'

            jpf {
              downloadUrl='$jpfUrl'
            }
        """
    }

    private File buildFileWithJUnit() {
        buildFile << """
            plugins {
              id 'com.github.upthewaterspout.jpf'
            }

            apply plugin: 'java'

            repositories {
              jcenter()
            }

            jpf {
              downloadUrl='$jpfUrl'
            }

            dependencies {
              testCompile 'junit:junit:4.12'
            }
        """
    }

    private BuildResult executeTask(String task) {
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('--stacktrace', task)
                .withPluginClasspath()
                .build()
        System.out.println("OUTPUT=" + result.output)
        return result
    }

    private List<String> getExpectedClasspath() {
        File projectDir = testProjectDir.getRoot();
        String projectDirPath = projectDir.getAbsolutePath();
        String jpfJar = new FileNameFinder().getFileNames(projectDirPath, "**/jpf.jar").first();
        String[] dirs = [projectDirPath + "/build/classes/java/main",
                       projectDirPath + "/build/resources/main",
                       projectDirPath + "/build/classes/java/test",
                       projectDirPath + "/build/resources/test",
                       jpfJar];
        return Arrays.asList(dirs).sort()
    }

    private List<String> getExpectedSourcepath() {
        File projectDir = testProjectDir.getRoot();
        String projectDirPath = projectDir.getAbsolutePath();
        String[] dirs = [projectDirPath + "/src/main/java",
                         projectDirPath + "/src/test/java"];
        return Arrays.asList(dirs).sort()
    }
}
