package com.github.upthewaterspout.jpfgradle;

import groovy.util.AntBuilder;

import java.io.File;
import java.util.LinkedHashMap;

public class AntHelper {
  public static void get(String sourceUrl, File target) {
    AntBuilder ant = new AntBuilder();
    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>(2);
    map.put("src", sourceUrl);
    map.put("dest", target);
    ant.invokeMethod("get", map);
  }

  public static void unzip(File sourceZip, File targetDir) {
    AntBuilder ant = new AntBuilder();
    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>(2);
    map.put("src", sourceZip);
    map.put("dest", targetDir);
    ant.invokeMethod("unzip", map);
  }

}
