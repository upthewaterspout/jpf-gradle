package com.github.upthewaterspout.jpfgradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.testing.Test;

/**
 * Plugin that downloads and configuration Java Path Finder (JPF)
 */
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
          task.setSourceSet(extension.getSourceSetSupplier());
          task.setProperties(extension.getPropertiesSupplier());
        });
    propertyFileTask.dependsOn(downloaderTask);
    return propertyFileTask;
  }

  private DownloadJpfTask addDownloadTask(final Project project,
                                          final JpfPluginExtension extension) {
    return project.getTasks()
          .create("downloadJpf", DownloadJpfTask.class, task -> {
            task.setDownloadUrl(extension.getDownloadUrlSupplier());
            task.setParentDir(extension.getInstallDirSupplier());
          });
  }

  /**
   * Add dependencies to the source set compile task and to test tasks
   * so that jpf will be downloaded and the properties file will be generated
   * when compiling and runing tests.
   */
  private void addDependenciesToJavaTasks(final Project project, final JpfPluginExtension extension,
                                          final DownloadJpfTask downloaderTask,
                                          final PropertyFileGeneratorTask propertyFileTask) {
    project.getPlugins().apply(JavaPlugin.class);
    JavaPluginConvention javaConvention =
        project.getConvention().getPlugin(JavaPluginConvention.class);

    project.afterEvaluate(p -> {
      SourceSet sourceSet = javaConvention.getSourceSets().getByName(extension.getSourceSetSupplier().get());
      project.getDependencies().add(sourceSet.getCompileConfigurationName(), project.files(downloaderTask));

      project.getTasks().withType(Test.class).forEach(task -> task.dependsOn(propertyFileTask));
    });
  }
}
