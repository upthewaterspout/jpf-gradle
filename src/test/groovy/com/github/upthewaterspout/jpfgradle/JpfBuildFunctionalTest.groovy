package com.github.upthewaterspout.jpfgradle

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import spock.lang.Specification


class JpfBuildFunctionalTest extends Specification {
    @Rule final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile
    String jpfUrl

    def setup() {
        buildFile = testProjectDir.newFile('build.gradle')
        jpfUrl = getClass().getResource("/jpf-core.zip").toString()
    }

    def "Adding the jpf plugin download and unzips jpf"() {
        given:
        defaultBuildFile()

        when:
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('test')
                .withPluginClasspath()
                .build()

        then:
        System.out.println("OUTPUT=" + result.output)
        result.task(':downloadJpf').outcome == TaskOutcome.SUCCESS
        new File(testProjectDir.getRoot(), ".jpf").exists()
    }

    def "Adding the jpf plugin generates a jpf.properties file with the correct source and classpath"() {
        given:
        defaultBuildFile()

        when:
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('generateJpfProperties')
                .withPluginClasspath()
                .build()

        then:
        System.out.println("OUTPUT=" + result.output)
        result.task(':generateJpfProperties').outcome == TaskOutcome.SUCCESS
        File propertiesFile = new File(testProjectDir.getRoot(), "jpf.properties");
        propertiesFile.exists();
        Properties props = new Properties();
        FileReader reader  = new FileReader(propertiesFile);
        try {
           props.load(reader)
        } finally {
            reader.close();
        }
        props.get("classpath").equals(getExpectedClasspath());
        props.get("sourcepath").equals(getExpectedSourcepath());
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

    private String getExpectedClasspath() {
        File projectDir = testProjectDir.getRoot();
        String projectDirPath = projectDir.getAbsolutePath();
        String jpfJar = new FileNameFinder().getFileNames(projectDirPath, "**/jpf.jar").first();
        String[] dirs = [projectDirPath + "build/classes/java/main",
                       projectDirPath + "build/classes/resources/main",
                       projectDirPath + "build/classes/java/test",
                       projectDirPath + "build/classes/resources/test",
                       jpfJar];
        return String.join(File.pathSeparator, dirs);
    }

    private String getExpectedSourcepath() {
        File projectDir = testProjectDir.getRoot();
        String projectDirPath = projectDir.getAbsolutePath();
        String[] dirs = [projectDirPath + "build/src/main/java",
                         projectDirPath + "build/src/test/java"];
        return String.join(File.pathSeparator, dirs);
    }
}
