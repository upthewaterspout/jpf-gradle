package com.github.upthewaterspout.jpfgradle.internal;

import groovy.util.AntBuilder;

import java.io.File;
import java.util.LinkedHashMap;

/**
 * Helper class to invoke the groovy ant builder to do a couple
 * of tasks related to downloading.
 */
public class AntHelper {

  /**
   * Use the ant get task to download the given url
   */
  public static void get(String sourceUrl, File target) {
    AntBuilder ant = new AntBuilder();
    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>(2);
    map.put("src", sourceUrl);
    map.put("dest", target);
    ant.invokeMethod("get", map);
  }

  /**
   * Use the ant unzip task to unzip the given zip file
   */
  public static void unzip(File sourceZip, File targetDir) {
    AntBuilder ant = new AntBuilder();
    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>(2);
    map.put("src", sourceZip);
    map.put("dest", targetDir);
    ant.invokeMethod("unzip", map);
  }

}
