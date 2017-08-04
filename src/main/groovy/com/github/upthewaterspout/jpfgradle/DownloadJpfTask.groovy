package com.github.upthewaterspout.jpfgradle

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskAction

import java.security.MessageDigest


class DownloadJpfTask extends DefaultTask {
    Provider<String> downloadURL
    Provider<String> parentDir

    DownloadJpfTask() {
        this.outputs.file(getJpfJar())
    }

    public Closure<File> getJpfJar() {
        return {
            return new File(getDownloadDir(), "build/jpf.jar")
        };
    }

    public File getDownloadDir() {
        MessageDigest digest = MessageDigest.getInstance("SHA-256")
        String downloadHash = digest.digest(downloadURL.get().getBytes("UTF-8")).encodeHex()
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

        ant.get(src: downloadURL.get(), dest: tempDownload)
        ant.unzip(src: tempDownload, dest: tmpDir)
        tempDownload.delete();
        tmpDir.renameTo(targetDir);

        return targetDir;
    }
}
