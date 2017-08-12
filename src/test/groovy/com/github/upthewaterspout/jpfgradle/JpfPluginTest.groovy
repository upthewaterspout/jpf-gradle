package com.github.upthewaterspout.jpfgradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class JpfPluginTest extends Specification {
    Project project;

    def setup() {
        project = ProjectBuilder.builder().build()
    }

    def "Adding the jpf plugin adds the expected tasks"() {
        given:

        when:
        project.pluginManager.apply("com.github.upthewaterspout.jpf")

        then:
        project.tasks.downloadJpf instanceof DownloadJpfTask
        project.tasks.generateJpfProperties instanceof PropertyFileGeneratorTask
    }

}
