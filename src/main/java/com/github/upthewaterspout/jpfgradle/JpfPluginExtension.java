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

import java.io.File;

public class JpfPluginExtension {
  private final PropertyState<String> installDir;
  private final PropertyState<String> downloadUrl;

  public JpfPluginExtension(Project project) {
    installDir = project.property(String.class);
    downloadUrl = project.property(String.class);
    installDir.set(new File(project.getProjectDir(), "/.jpf").getAbsolutePath());
    downloadUrl.set(
        "https://babelfish.arc.nasa.gov/trac/jpf/raw-attachment/wiki/projects/jpf-core/jpf-core-r32.zip");
  }

  public Provider<String> getDownloadUrlProvider() {
    return downloadUrl;
  }

  public Provider<String> getInstallDirProvider() {
    return installDir;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl.set(downloadUrl);
  }

  public void setInstallDir(String installDir) {
    this.installDir.set(installDir);
  }

}
