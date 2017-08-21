package com.github.upthewaterspout.jpfgradle;

import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaPlugin;

public class JpfPlugin implements Plugin<Project> {
  public static final String EXTENSION_NAME = "jpf";

  @Override
  public void apply(final Project project) {
    JpfPluginExtension extension = project.getExtensions().create(EXTENSION_NAME, JpfPluginExtension.class, project);

    DownloadJpfTask downloaderTask = project.getTasks()
            .create("downloadJpf", DownloadJpfTask.class, task -> {
                task.setDownloadUrl(extension.getDownloadUrlProvider());
                task.setParentDir(extension.getInstallDirProvider());
            });

    PropertyFileGeneratorTask propertyFileTask = project.getTasks()
        .create("generateJpfProperties", PropertyFileGeneratorTask.class);
    propertyFileTask.dependsOn(downloaderTask);

    project.getPlugins().apply(JavaPlugin.class);
    project.afterEvaluate(p -> {
        project.getDependencies().add("testCompile", project.files(downloaderTask));
        project.getTasks().getByName("test").dependsOn(propertyFileTask);
      });
  }
}
