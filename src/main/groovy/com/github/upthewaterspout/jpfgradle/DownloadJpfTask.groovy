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

import org.gradle.api.DefaultTask
import org.gradle.api.provider.PropertyState
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import java.security.MessageDigest


class DownloadJpfTask extends DefaultTask {
    PropertyState<String> downloadUrl
    PropertyState<String> parentDir

    DownloadJpfTask() {
        downloadUrl = getProject().property(String.class)
        parentDir = getProject().property(String.class)
        this.outputs.file(getJpfJar())
    }

    void setDownloadUrl(final Provider<String> downloadUrl) {
        this.downloadUrl.set(downloadUrl)
    }

    void setParentDir(final Provider<String> parentDir) {
        this.parentDir.set(parentDir)
    }

    @Input
    public String getDownloadUrl() {
        return downloadUrl.get()
    }

    @Input
    public String getParentDir() {
        return parentDir.get();
    }

    public Closure<File> getJpfJar() {
        return {
            return new File(getDownloadDir(), "jpf-core/build/jpf.jar")
        };
    }

    public File getDownloadDir() {
        MessageDigest digest = MessageDigest.getInstance("SHA-256")
        String downloadHash = digest.digest(downloadUrl.get().getBytes("UTF-8")).encodeHex()
        return new File(parentDir.get(), downloadHash)
    }

    /**
     * Download the file and return the location of the installation
     * @return
     */
    @TaskAction
    public File download() {

        File targetDir = getDownloadDir()
        if(targetDir.exists()) {
            return targetDir;
        }

        File tmpDir = new File(targetDir.getAbsolutePath() + ".tmp");
        tmpDir.deleteDir();

        def ant = new AntBuilder();

        File tempDownload = File.createTempFile("jpfdownload-", ".tmp");

        ant.get(src: downloadUrl.get(), dest: tempDownload)
        ant.unzip(src: tempDownload, dest: tmpDir)
        tempDownload.delete();
        tmpDir.renameTo(targetDir);

        return targetDir;
    }
}
