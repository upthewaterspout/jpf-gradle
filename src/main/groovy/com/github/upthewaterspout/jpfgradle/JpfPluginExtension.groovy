package com.github.upthewaterspout.jpfgradle

import org.gradle.api.Project
import org.gradle.api.provider.PropertyState
import org.gradle.api.provider.Provider


class JpfPluginExtension {
    private final PropertyState<String> installDir
    private final PropertyState<String> downloadUrl

    def JpfPluginExtension(Project project) {
        installDir = project.property(String)
        downloadUrl = project.property(String)
        installDir.set(new File(project.projectDir,'/.jpf').absolutePath)
        downloadUrl.set('https://babelfish.arc.nasa.gov/trac/jpf/raw-attachment/wiki/projects/jpf-core/jpf-core-r32.zip')
    }

    public Provider<String> getDownloadUrlProvider() {
        return downloadUrl;
    }

    public Provider<String> getInstallDirProvider() {
        return installDir;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl.set(downloadUrl)
    }

    public void setInstallDir(String installDir) {
        this.installDir.set(installDir)
    }
}
