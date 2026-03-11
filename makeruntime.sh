#!/bin/bash

# This script builds the runtime images from the various JDKs, which must first be
# downloaded from https://jdk.java.net and expanded into ./jdks.

modpath=target/weavedreamer-0.2.10.02-PRERELEASE.jar
modpath=${modpath}:$HOME/.m2/repository/org/apache/commons/commons-configuration2/2.13.0/commons-configuration2-2.13.0.jar
modpath=${modpath}:$HOME/.m2/repository/org/apache/commons/commons-lang3/3.20.0/commons-lang3-3.20.0.jar
modpath=${modpath}:$HOME/.m2/repository/commons-logging/commons-logging/1.3.5/commons-logging-1.3.5.jar
modpath=${modpath}:$HOME/.m2/repository/org/apache/commons/commons-text/1.14.0/commons-text-1.14.0.jar


for arch in linux_arm linux_x64 mac_x64 windows mac_arm; do
  rm -rf runtime/$arch
  jlink  --module-path jdks/${arch}/jdk-25.0.2/jmods:$modpath --output runtime/$arch --add-modules com.jenkins.weavedreamer
done

