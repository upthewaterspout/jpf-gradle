package com.github.upthewaterspout.jpfgradle

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet

class JpfPlugin implements Plugin<Project> {
    public static final String EXTENSION_NAME = 'jpf'

    @Override
    void apply(final Project project) {
        def extension = project.extensions.create(EXTENSION_NAME, JpfPluginExtension, project)

        def downloaderTask = project.tasks.create('downloadJpf', DownloadJpfTask.class, new Action<DownloadJpfTask>() {
            public void execute(DownloadJpfTask task) {
                task.setDownloadUrl(extension.getDownloadUrlProvider())
                task.setParentDir(extension.getInstallDirProvider())
            }
        })

        def propertyFileTask = project.tasks.create('generateJpfProperties', PropertyFileGeneratorTask) {
            //TODO - allow configuration of jpf properties?
        }
        propertyFileTask.dependsOn(downloaderTask)

        project.getPlugins().apply(JavaPlugin.class);
//        JavaPluginConvention javaConvention =
//                project.getConvention().getPlugin(JavaPluginConvention.class);
//        SourceSet test = javaConvention.getSourceSets().getByName(SourceSet.TEST_SOURCE_SET_NAME);
//        test.compileClasspath.add({downloaderTask})
        project.afterEvaluate {
            project.dependencies.add("testCompile", project.files({downloaderTask}))
            project.tasks.getByName("test").dependsOn(propertyFileTask)
        }
    }
}
