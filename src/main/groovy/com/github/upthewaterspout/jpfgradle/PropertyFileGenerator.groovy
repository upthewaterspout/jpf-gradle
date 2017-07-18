package com.github.upthewaterspout.jpfgradle

import org.gradle.api.DefaultTask
import org.gradle.api.internal.TaskInputsInternal
import org.gradle.api.internal.TaskOutputsInternal
import org.gradle.api.tasks.TaskAction

class PropertyFileGeneratorTask extends DefaultTask {

    private final File outputFile;

    PropertyFileGeneratorTask() {
        outputFile =project.file("${project.rootDir}/jpf.properties");
        this.outputs.file(outputFile);
    }

    @TaskAction
    generateJpfProperties() {
        Properties properties = new Properties();
        properties.setProperty("classpath", project.configurations["testCompile"].dependencies.collect().join(File.pathSeparator))
        properties.setProperty("sourcepath", project.sourceSets.flatMap({sourceSet -> sourceSet.java}).join(File.pathSeparator));

        new FileOutputStream(outputFile).withStream {
            fos -> properties.store(fos, 'Generated by jpf-gradle plugin. Do not edit by hand');
        }

    }
}