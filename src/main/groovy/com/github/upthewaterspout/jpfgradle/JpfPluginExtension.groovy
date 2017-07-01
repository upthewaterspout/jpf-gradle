package com.github.upthewaterspout.jpfgradle

import org.gradle.api.Project
import org.gradle.api.provider.PropertyState


class JpfPluginExtension {
    PropertyState<String> installDir
    PropertyState<String> downloadURL

    def JpfPluginExtension(Project project) {
        installDir = project.property(String)
        downloadURL = project.property(String)
        installDir.set(new File(project.projectDir,'/.jpf').absolutePath)
        downloadURL.set('https://babelfish.arc.nasa.gov/trac/jpf/raw-attachment/wiki/projects/jpf-core/jpf-core-r32.zip')
    }

    def getDownloadURLProvider() {
        return downloadURL;
    }

    def getInstallDirProvider() {
        return installDir;
    }
}
