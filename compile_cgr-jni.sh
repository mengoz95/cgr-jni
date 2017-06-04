#!/bin/bash
if [ ! -d "$CGR_JNI_CLASSPATH" ]; then 
	mkdir $CGR_JNI_CLASSPATH; 
fi

SOURCES=$(find src -iname "*.java" | grep -v src/test/)
echo "SOURCES: $SOURCES"


#javac -cp $ONE_BIN:$CGR_JNI_CLASSPATH -d $CGR_JNI_CLASSPATH ./src/routing/*.java ./src/*.java ./src/cgr_jni/*.java ./src/report/*.java ./src/core/*.java ./src/input/*.java
javac -cp $ONE_BIN:$CGR_JNI_CLASSPATH -d $CGR_JNI_CLASSPATH $SOURCES
