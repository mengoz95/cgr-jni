#! /bin/bash

echo "--> Insert full path of the java class \"ContactPlanCreator.java\" and \"ContactPlanLine.java\"" 
echo "(e.g. /home/USER/workspace/cgr-jni-merge/src/cp_creator)"

read path

cd $path

javac ContactPlanCreator.java ContactPlanLine.java

java ContactPlanCreator 

rm ContactPlanCreator.class ContactPlanLine.class


