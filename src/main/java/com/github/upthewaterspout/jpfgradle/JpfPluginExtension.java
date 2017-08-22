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

import org.gradle.api.Project;
import org.gradle.api.provider.PropertyState;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.SourceSet;

import java.io.File;
import java.util.Collections;
import java.util.Map;

public class JpfPluginExtension {
  public static final String SOURCE_SET_NAME = SourceSet.TEST_SOURCE_SET_NAME;
  private final PropertyState<String> installDir;
  private final PropertyState<String> downloadUrl;
  private final PropertyState<String> sourceSet;
  private final PropertyState<Map> properties;

  public JpfPluginExtension(Project project) {
    installDir = project.property(String.class);
    installDir.set(new File(project.getProjectDir(), "/.jpf").getAbsolutePath());

    downloadUrl = project.property(String.class);
    downloadUrl.set(
        "https://babelfish.arc.nasa.gov/trac/jpf/raw-attachment/wiki/projects/jpf-core/jpf-core-r32.zip");

    sourceSet = project.property(String.class);
    sourceSet.set(SOURCE_SET_NAME);

    properties = project.property(Map.class);
    properties.set(Collections.emptyMap());
  }

  public Provider<String> getDownloadUrlProvider() {
    return downloadUrl;
  }

  public Provider<String> getInstallDirProvider() {
    return installDir;
  }

  public Provider<String> getSourceSetProvider() {
    return sourceSet;
  }

  public Provider<Map> getPropertiesProvider() {
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
