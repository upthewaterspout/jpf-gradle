package com.github.upthewaterspout.jpfgradle;

import org.codehaus.groovy.runtime.EncodingGroovyMethods;
import org.codehaus.groovy.runtime.ResourceGroovyMethods;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.PropertyState;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;

public class DownloadJpfTask extends DefaultTask {
  private PropertyState<String> downloadUrl;
  private PropertyState<String> parentDir;

  public DownloadJpfTask() {
    downloadUrl = getProject().property(String.class);
    parentDir = getProject().property(String.class);
    getOutputs().file((Callable) () -> getJpfJar());
  }

  @Input
  public String getDownloadUrl() {
    return downloadUrl.get();
  }

  public void setDownloadUrl(final Provider<String> downloadUrl) {
    this.downloadUrl.set(downloadUrl);
  }

  @Input
  public String getParentDir() {
    return parentDir.get();
  }

  public void setParentDir(final Provider<String> parentDir) {
    this.parentDir.set(parentDir);
  }

  @Internal
  private File getJpfJar() {
    return new File(getDownloadDir(), "jpf-core/build/jpf.jar");
  }

  @Internal
  private File getDownloadDir() {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(downloadUrl.get().getBytes("UTF-8"));
      String downloadHash = EncodingGroovyMethods.encodeHex(hashBytes).toString();
      return new File(parentDir.get(), downloadHash);
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Download the file and return the location of the installation
   */
  @TaskAction
  public File download() throws IOException {

    File targetDir = getDownloadDir();
    if (targetDir.exists()) {
      return targetDir;
    }

    File tmpDir = new File(targetDir.getAbsolutePath() + ".tmp");
    ResourceGroovyMethods.deleteDir(tmpDir);

    File tempDownload = File.createTempFile("jpfdownload-", ".tmp");

    AntHelper.get(downloadUrl.get(), tempDownload);
    AntHelper.unzip(tempDownload, tmpDir);
    tempDownload.delete();
    tmpDir.renameTo(targetDir);

    return targetDir;
  }
}
