export WORKING_DIR=$(pwd)
export THEONE_DIR=$WORKING_DIR/the-one
export CGRJNI_DIR=$WORKING_DIR/cgr-jni
# get cgr-jni code from upstream
git clone https://github.com/alessandroberlati/cgr-jni.git
cd $CGRJNI_DIR
# switch to Merge branch
git checkout Merge
git apply $WORKING_DIR/cgr-jni-compilationfix.patch

cd $WORKING_DIR
# get The ONE code from upstream
git clone https://github.com/akeranen/the-one.git
cd $THEONE_DIR
# switch to 1.6.0 version
git checkout v1.6.0
git checkout -b cgr-jni

# apply patch
git apply $WORKING_DIR/the-one_v1.6.0-cgr-jni.patch

# compile the one
./compile.bat

export ONE_BIN=$WORKING_DIR/the-one/
export CGR_JNI_CLASSPATH=$WORKING_DIR/cgr-jni/bin/ 

# compile cgr-jni java classes
cd $CGRJNI_DIR
chmod +x compile_cgr-jni.sh
./compile_cgr-jni.sh

# compile cgr-jni native C code
cd $CGRJNI_DIR/ion_cgr_jni
make ONE_CLASSPATH=$THEONE_DIR

# prepare runtime env variables
export LD_LIBRARY_PATH=$CGRJNI_DIR/ion_cgr_jni/
export CGR_JNI_CLASSPATH=$WORKING_DIR/cgr-jni/bin/ 

# run simulation
cd $THEONE_DIR
# ./one.sh config_file.txt