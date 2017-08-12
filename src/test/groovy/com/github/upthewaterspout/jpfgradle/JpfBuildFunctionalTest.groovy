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
}
