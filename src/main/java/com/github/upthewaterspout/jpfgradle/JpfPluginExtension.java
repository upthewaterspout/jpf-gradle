/**
 * Copyright 2017 Dan Smith
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.upthewaterspout.jpfgradle;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;

/**
 * Configuration extension for jpf configuration options
 */
public class JpfPluginExtension {
  public static final String SOURCE_SET_NAME = SourceSet.TEST_SOURCE_SET_NAME;
  private final Property<String> installDir = new Property<String>();
  private final Property<String> downloadUrl = new Property<String>();
  private final Property<String> sourceSet = new Property<String>();
  private final Property<Map> properties = new Property<Map>();

  public JpfPluginExtension(Project project) {
    installDir.set(new File(project.getProjectDir(), "/.jpf").getAbsolutePath());

    downloadUrl.set(
        "https://babelfish.arc.nasa.gov/trac/jpf/raw-attachment/wiki/projects/jpf-core/jpf-core-r32.zip");

    sourceSet.set(SOURCE_SET_NAME);

    properties.set(Collections.emptyMap());
  }

  public Supplier<String> getDownloadUrlSupplier() {
    return downloadUrl;
  }

  public Supplier<String> getInstallDirSupplier() {
    return installDir;
  }

  public Supplier<String> getSourceSetSupplier() {
    return sourceSet;
  }

  public Supplier<Map> getPropertiesSupplier() {
    return properties;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl.set(downloadUrl);
  }

  public void setInstallDir(String installDir) {
    this.installDir.set(installDir);
  }

  public void setSourceSet(String sourceSet) {
    this.sourceSet.set(sourceSet);
  }

  public void setProperties(Map properties) {
    this.properties.set(properties);
  }

}
