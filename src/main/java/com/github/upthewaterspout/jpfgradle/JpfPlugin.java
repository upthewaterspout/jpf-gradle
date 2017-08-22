package com.github.upthewaterspout.jpfgradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.testing.Test;

public class JpfPlugin implements Plugin<Project> {
  public static final String EXTENSION_NAME = "jpf";

  @Override
  public void apply(final Project project) {
    JpfPluginExtension extension = addConfigurationExtension(project);

    DownloadJpfTask downloaderTask = addDownloadTask(project, extension);

    PropertyFileGeneratorTask
        propertyFileTask =
        addPropertyFileTask(project, extension, downloaderTask);

    addDependenciesToJavaTasks(project, extension, downloaderTask, propertyFileTask);
  }

  private JpfPluginExtension addConfigurationExtension(final Project project) {
    return project.getExtensions().create(EXTENSION_NAME, JpfPluginExtension.class, project);
  }

  private PropertyFileGeneratorTask addPropertyFileTask(final Project project,
                                                        final JpfPluginExtension extension,
                                                        final DownloadJpfTask downloaderTask) {
    PropertyFileGeneratorTask propertyFileTask = project.getTasks()
        .create("generateJpfProperties", PropertyFileGeneratorTask.class, task -> {
          task.setSourceSet(extension.getSourceSetProvider());
          task.setProperties(extension.getPropertiesProvider());
        });
    propertyFileTask.dependsOn(downloaderTask);
    return propertyFileTask;
  }

  private DownloadJpfTask addDownloadTask(final Project project,
                                          final JpfPluginExtension extension) {
    return project.getTasks()
          .create("downloadJpf", DownloadJpfTask.class, task -> {
            task.setDownloadUrl(extension.getDownloadUrlProvider());
            task.setParentDir(extension.getInstallDirProvider());
          });
  }

  private void addDependenciesToJavaTasks(final Project project, final JpfPluginExtension extension,
                                          final DownloadJpfTask downloaderTask,
                                          final PropertyFileGeneratorTask propertyFileTask) {
    project.getPlugins().apply(JavaPlugin.class);
    JavaPluginConvention javaConvention =
        project.getConvention().getPlugin(JavaPluginConvention.class);

    project.afterEvaluate(p -> {
      SourceSet sourceSet = javaConvention.getSourceSets().getByName(extension.getSourceSetProvider().get());
      project.getDependencies().add(sourceSet.getCompileConfigurationName(), project.files(downloaderTask));

      project.getTasks().withType(Test.class).forEach(task -> task.dependsOn(propertyFileTask));
    });
  }
}
