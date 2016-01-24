#!/bin/bash
GRADING_DIR=$HOME/grading
TMP_DIR=/tmp/p1-grading/
REQUIRED_FILES="team.txt actors.sql ComputeSHA.java"


# usage
if [ $# -ne 1 ]
then
     echo "Usage: $0 project1.zip" 1>&2
     exit
fi

if [ `hostname` != "class-vm" ]; then
     echo "ERROR: You need to run this script within the class virtual machine" 1>&2
     exit
fi

ZIP_FILE=$1

# clean any existing files
rm -rf ${TMP_DIR}
mkdir ${TMP_DIR}

# unzip the submission zip file 
if [ ! -f ${ZIP_FILE} ]; then
    echo "ERROR: Cannot find $ZIP_FILE" 1>&2
    rm -rf ${TMP_DIR}
    exit 1
fi
unzip -q -d ${TMP_DIR} ${ZIP_FILE}
if [ "$?" -ne "0" ]; then 
    echo "ERROR: Cannot unzip ${ZIP_FILE} to ${TMP_DIR}"
    rm -rf ${TMP_DIR}
    exit 1
fi

# change directory to the grading folder
cd ${TMP_DIR}

# check the existence of the required files
for FILE in ${REQUIRED_FILES}
do
    if [ ! -f ${FILE} ]; then
        echo "ERROR: Cannot find ${FILE} in the root folder of your zip file" 1>&2
        rm -rf ${TMP_DIR}
        exit 1
    fi
done

# run actors.sql script
echo "drop table if exists Actors;" | mysql TEST
echo "Running your actors.sql script..."
mysql TEST < actors.sql
echo "drop table if exists Actors;" | mysql TEST
echo "Finished running actors.sql" 
echo
echo

# compile, exit if error
echo "Compiling ComputeSHA.java..."
javac ComputeSHA.java
if [ "$?" -ne "0" ] 
then
	echo "ERROR: Compilation of ComputeSHA.java failed" 1>&2
	rm -rf ${TMP_DIR}
	exit 1
fi

# test ComputeSHA.java, using test input files
INPUT_FILE="${TMP_DIR}/test-input.txt"
echo "A simple test input file to check ComputeSHA" > ${INPUT_FILE}
RESULT1=`java ComputeSHA ${INPUT_FILE}`
RESULT2=`sha1sum ${INPUT_FILE} | awk '{print $1}'`

if [ "$RESULT1" = "$RESULT2" ]; then
	echo "SUCCESS!" 1>&2
else
	echo "ERROR: ComputeSHA computes an incorrect SHA value." 1>&2
	rm -rf ${TMP_DIR}
	exit 1
fi

# clean up
rm -rf ${TMP_DIR}
exit 0
