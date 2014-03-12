#!/bin/bash

# This script will unzip the distribution archive and run some Abbot
# acceptance tests on it.
# The Abbot scripts must use the same directory as this extracts to 
# to find the application. Currently that is not verified and is 
# fragile.
# The Abbot libraries from http://sourceforge.net/projects/abbot/
# have been incorporated into the source tree in libs/abbot-1.3.0.
# Some of the libraries distributed with Abbot are omitted for 
# compactness. In particular I assume JUnit is already available.

JAVA_LIB=/usr/share/java

show_usage_and_quit() {
  echo Usage: $0 zipfile
  echo Tries to unzip and test the file, 
  echo which is expected to be a Freeweave distribution
  exit
}

if [ ! $1 -a -e $1 ] 
then
  show_usage_and_quit
fi

TMPDIR=GUItest/temp
if [ -e $TMPDIR ] 
then
  rm -rf $TMPDIR
fi	
unzip $1 -d $TMPDIR

$JAVA_LIB/jdk1.6.0_35/jre/bin/java -classpath $JAVA_LIB/junit.jar:build/GUItest:$TMPDIR/WeavingSimulator.jar:libs/uispec4j-2.4-jdk16.jar junit.textui.TestRunner com.jenkins.weavingsimulator.uat.BasicDraftEdit
