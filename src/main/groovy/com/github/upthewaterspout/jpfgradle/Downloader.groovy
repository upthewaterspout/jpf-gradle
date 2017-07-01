package com.github.upthewaterspout.jpfgradle

import org.apache.tools.ant.taskdefs.Get
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskAction
import org.gradle.wrapper.Download

import java.security.MessageDigest


class Downloader extends DefaultTask {
    String downloadURL
    String parentDir
    private File targetDir

    Downloader(String downloadURL, String parentDir) {
        this.downloadURL = downloadURL;
        this.parentDir = parentDir;
        MessageDigest digest = MessageDigest.getInstance("SHA-256")
        String downloadHash = digest.digest(downloadURL.getBytes("UTF-8")).encodeHex();
        File targetDir = new File(parentDir, downloadHash.encodeHex())
    }

    public File getDir() {
        return targetDir;
    }

    /**
     * Download the file and return the location of the installation
     * @return
     */
    @TaskAction
    public File download() {

        if(targetDir.exists()) {
            return targetDir;
        }

        File tmpDir = new File(targetDir.getAbsolutePath() + ".tmp");
        tmpDir.deleteDir();

        def ant = new AntBuilder();

        File tempDownload = File.createTempFile("jpfdownload-", ".tmp");

        ant.get(src: downloadURL, dest: tempDownload)
        ant.unzip(src: tempDownload, dest: tmpDir)
        tempDownload.delete();
        tmpDir.renameTo(targetDir);

        return targetDir;
    }
}
