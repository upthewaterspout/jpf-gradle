package com.github.upthewaterspout.jpfgradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration


class JpfPlugin implements Plugin<Project> {


    public static final String EXTENSION_NAME = 'jpf'
    public static final String CONFIGURATION_NAME = 'jpfTest'

    @Override
    void apply(final Project target) {
        target.extensions.create(EXTENSION_NAME, JpfPluginExtension)
        target.tasks.create('downloadJpf', Downloader) {

        }
//        target.afterEvaluate( {
//            Configuration jpfTest = target.configurations.create(CONFIGURATION_NAME);
//            jpfTest.extendsFrom(target.configurations['test']);
//        })

    }
}
