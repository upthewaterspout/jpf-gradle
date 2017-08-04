package com.github.upthewaterspout.jpfgradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class JpfPlugin implements Plugin<Project> {
    public static final String EXTENSION_NAME = 'jpf'

    @Override
    void apply(final Project target) {
        def extension = target.extensions.create(EXTENSION_NAME, JpfPluginExtension, target)

        def downloaderTask = target.tasks.create('downloadJpf', DownloadJpfTask) {
            downloadURL = extension.downloadURLProvider
            parentDir = extension.installDirProvider
        }

        def propertyFileTask = target.tasks.create('generateJpfProperties', PropertyFileGeneratorTask) {
            //TODO - allow configuration of jpf properties?
        }
        target.afterEvaluate {
            target.dependencies.add("testCompile", target.files({downloaderTask}))
        }
    }
}
