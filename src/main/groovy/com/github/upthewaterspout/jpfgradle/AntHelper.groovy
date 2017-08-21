package com.github.upthewaterspout.jpfgradle

class AntHelper {

    public static void get(String sourceUrl, File target) {
        def ant = new AntBuilder();
        ant.get(src: sourceUrl, dest: target)
    }

    public static void unzip(File sourceZip, File targetDir) {
        def ant = new AntBuilder();
        ant.unzip(src: sourceZip, dest: targetDir)
    }

}
