package com.github.upthewaterspout.jpfgradle;

import com.github.upthewaterspout.jpfgradle.internal.AntHelper;
import org.codehaus.groovy.runtime.EncodingGroovyMethods;
import org.codehaus.groovy.runtime.ResourceGroovyMethods;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * Task that downloads a a jpf zip file and  extracts it.
 * If jpf is already extracted, this task does nothing.
 *
 * The task creates a directory based on the hash of the download url,
 * so if the download url changes it will create a new directory.
 */
public class DownloadJpfTask extends DefaultTask {
  private Property<String> downloadUrl = new Property<String>();
  private Property<String> parentDir = new Property<String>();

  public DownloadJpfTask() {
    getOutputs().file((Callable) () -> getJpfJar());
  }

  /**
   * The URL to download jpf from. Must point to a zip file of jpf-core that
   * has been built.
   */
  @Input
  public String getDownloadUrl() {
    return downloadUrl.get();
  }

  public void setDownloadUrl(final Supplier<String> downloadUrl) {
    this.downloadUrl.set(downloadUrl);
  }

  /**
   * The target directory to download jpf into
   */
  @Input
  public String getParentDir() {
    return parentDir.get();
  }

  public void setParentDir(final Supplier<String> parentDir) {
    this.parentDir.set(parentDir);
  }

  @Internal
  private File getJpfJar() {
    return new File(getDownloadDir(), "jpf-core/build/jpf.jar");
  }

  /**
   * Create a directory based on the hash of the download url to store the unzipped installation
   */
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

    //Do nothing if we've already downloaded and extracted jpf
    File targetDir = getDownloadDir();
    if (targetDir.exists()) {
      return targetDir;
    }

    //Download the jpf zip to a temporary file
    File tempDownload = File.createTempFile("jpfdownload-", ".tmp");
    tempDownload.deleteOnExit();
    AntHelper.get(downloadUrl.get(), tempDownload);

    //Unzip to a temporary directory
    File tmpDir = new File(targetDir.getAbsolutePath() + ".tmp");
    ResourceGroovyMethods.deleteDir(tmpDir);
    AntHelper.unzip(tempDownload, tmpDir);
    tempDownload.delete();

    //Rename the temporary directory to the actual directory
    tmpDir.renameTo(targetDir);

    return targetDir;
  }
}
