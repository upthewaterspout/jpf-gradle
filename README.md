# Java Path Finder Gradle Plugin

This is a gradle plugin that makes it easier to use [Java Path Finder](https://babelfish.arc.nasa.gov/trac/jpf) (JPF), install it,
from within a gradle project, without having to install JPF our configure a jpf.properties with your projects classpath.

This plugin is in the early stages of development.


##Usage
Add this this plugin in your gradle build using the [configuration](https://plugins.gradle.org/plugin/com.github.upthewaterspout.jpf) from plugins.gradle.org.


By default this plugin will download JPF
to a .jpf directory within the root of your project, add jpf.jar to your
testCompile classpath, and generate a jpf.properties file that contains
classpath and sourcepath variables generated from your projects configuration

###Example build file

See the [jpf-gradle-sample](https://github.com/upthewaterspout/jpf-gradle-sample) project for an an example
of how to use this plugin in your project.
```
plugins {
  id "com.github.upthewaterspout.jpf" version "0.1"
}

jpf {
}
```

####Optional Configuration
```
plugins {
  id "com.github.upthewaterspout.jpf" version "0.1"
}

jpf {
    downloadUrl='https://babelfish.arc.nasa.gov/trac/jpf/raw-attachment/wiki/projects/jpf-core/jpf-core-r32.zip'
    installDir=".jpf"
}
```

## TODO

- Mirror jpf-core somewhere
- Allow the user to configure the source set to target
- Allow configuration of other jpf properties
- Allow downloading other jpf components
- Figure out how to configure the classpath to use IDE classes directories, if possible.
