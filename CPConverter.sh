#! /bin/bash

echo "--> Insert full path of the java classes \"ContactPlanCreator.java\" and \"ContactPlanLine.java\"" 
echo "(e.g. /home/USER/workspace/cgr-jni/src/)"

read path

cd $path

javac ContactPlanCreator.java ContactPlanLine.java

java ContactPlanCreator 

rm ContactPlanCreator.class ContactPlanLine.class


