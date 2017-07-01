package com.github.upthewaterspout.jpfgradle

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

    def setup() {
        buildFile = testProjectDir.newFile('build.gradle')
    }

    def "Adding the jpf plugin download and unzips jpf"() {
        given:
        buildFile << """
            plugins {
                id 'com.github.upthewaterspout.jpfgradle'
            }

            apply plugin: 'java'
        """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('test')
                .withPluginClasspath()
                .build()

        then:
        System.out.println("OUTPUT=" + result.output)
//        result.task(":test").outcome == TaskOutcome.SUCCESS
        new File(testProjectDir.getRoot(), ".jpf").exists()
    }
}
