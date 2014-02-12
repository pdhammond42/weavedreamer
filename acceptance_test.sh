#!/bin/bash

# This script will unzip the distribution archive and run some Abbot
# acceptance tests on it.
# The Abbot scripts must use the same directory as this extracts to 
# to find the application. Currently that is not verified and is 
# fragile.

JAVA_LIB=/usr/share/java

show_usage_and_quit() {
  echo Usage: $0 zipfile
  echo Tries to unzip and test the file, 
  echo which is expected to be a Freeweave distribution
  exit
}

if [ ! $1 -o ! -e $1 ] 
then
  show_usage_and_quit
fi

TMPDIR=test/temp
if [ -e $TMPDIR ] 
then
  rm -rf $TMPDIR
fi	
unzip $1 -d $TMPDIR

java -classpath libs/abbot-1.3.0/costello.jar:$JAVA_LIB/junit.jar junit.extensions.abbot.ScriptTestSuite test/acceptance

