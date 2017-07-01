package com.github.upthewaterspout.jpfgradle

import org.apache.tools.ant.taskdefs.Get
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskAction
import org.gradle.wrapper.Download

import java.security.MessageDigest


class Downloader extends DefaultTask {
    Provider<String> downloadURL
    Provider<String> parentDir

    Downloader() {
        this.outputs.file(getDir())
    }

    public Closure<File> getDir() {
        return {
            MessageDigest digest = MessageDigest.getInstance("SHA-256")
            String downloadHash = digest.digest(downloadURL.get().getBytes("UTF-8")).encodeHex();
            return new File(parentDir.get(), downloadHash)
        };
    }

    /**
     * Download the file and return the location of the installation
     * @return
     */
    @TaskAction
    public File download() {

        File targetDir = getDir().call()
        if(targetDir.exists()) {
            return targetDir;
        }

        File tmpDir = new File(targetDir.getAbsolutePath() + ".tmp");
        tmpDir.deleteDir();

        def ant = new AntBuilder();

        File tempDownload = File.createTempFile("jpfdownload-", ".tmp");

        ant.get(src: downloadURL.get(), dest: tempDownload)
        ant.unzip(src: tempDownload, dest: tmpDir)
        tempDownload.delete();
        tmpDir.renameTo(targetDir);

        return targetDir;
    }
}
