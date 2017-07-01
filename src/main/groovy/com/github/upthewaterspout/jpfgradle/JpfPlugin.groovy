package com.github.upthewaterspout.jpfgradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.tasks.testing.Test


class JpfPlugin implements Plugin<Project> {
    public static final String EXTENSION_NAME = 'jpf'

    @Override
    void apply(final Project target) {
        def extension = target.extensions.create(EXTENSION_NAME, JpfPluginExtension, target)

        def downloaderTask = target.tasks.create('downloadJpf', Downloader) {
            downloadURL = extension.downloadURLProvider
            parentDir = extension.installDirProvider
        }
        target.afterEvaluate {
            target.dependencies.add("testCompile", target.files(downloaderTask))
        }
    }
}
