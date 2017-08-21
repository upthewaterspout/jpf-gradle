# jpfgradle

This is the start of a gradle plugin that will download jpf, install it,
create a jpf configuration file and add jpf to the classpath of the test
target.

This is in the early stages of development.

## Current Status

The project does currently download and install jpf-core under the .jpf
directory in the project's directory. jpf.jar is added to the test classpath,
and a jpf.properties file is generated. A simple test project using this plugin
can run a test that uses jpf-core, see jpf-gradle-sample.

## TODO

- Add a end to end integration test that actually runs a jpf test
- Convert plugin code from groovy to java
- Mirror jpf-core somewhere
- Allow the user to configure the source set to target
- Allow configuration of other jpf properties
- Upload plugin to gradle plugin directory
- Allow downloading other jpf components
- Figure out how to configure the classpath to use IDE classes directories, if possible.
