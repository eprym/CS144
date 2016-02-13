#!/bin/bash
GRADING_DIR=$HOME/grading
TMP_DIR=/tmp/p3a-grading
REQUIRED_FILES="team.txt buildSQLIndex.sql dropSQLIndex.sql build.xml README.txt"

# usage
if [ $# -ne 1 ]
then
     echo "Usage: $0 project3-indexer.zip" 1>&2
     exit 1
fi
ZIP_FILE=$1

# make sure that the script runs on VM
if [ `hostname` != "class-vm" ]; then
     echo "ERROR: You need to run this script within the class virtual machine" 1>&2
     exit 1
fi

# clean any existing files
rm -rf ${TMP_DIR}

# create temporary directory used for grading
mkdir ${TMP_DIR}

#
# check Part A submission
#

# unzip the parta zip file
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

# change directory to the parta folder
cd ${TMP_DIR}

# check the existence of the required files
for FILE in ${REQUIRED_FILES}
do
    if [ ! -f ${FILE} ]; then
	echo "ERROR: Cannot find ${FILE} in the root folder of ${ZIP_FILE}" 1>&2
	rm -rf ${TMP_DIR}
	exit 1
    fi
done
JAVA_FILES=`find src -name '*.java' -print`
if [ -z "${JAVA_FILES}" ]; then
    echo "ERROR: No java file is included in src folder of ${ZIP_FILE}" 1>&2
    rm -rf ${TMP_DIR}
    exit 1
fi

# drop existing SQL index
echo "Dropping existing SQL index from dropSQLIndex.sql..."
mysql CS144 < dropSQLIndex.sql

# create SQL index
echo "Creating SQL index from buildSQLIndex.sql..."
mysql CS144 < buildSQLIndex.sql

# remove existing lucene index
echo "Removing existing lucene index..."
rm -rf /var/lib/lucene/*

# run ant script to build lucene index(ex)
echo "Running 'ant compile' to compile your indexer..."
ant compile

# run ant script to build lucene index(ex)
echo "Running 'ant run' to create lucene index(ex)..."
ant run

# print out status message
echo "Finished creating lucene index"

# check the content of /var/lib/lucene directory
if [[ -n $(ls) ]]; then
    echo "This is the current content of the lucene index directory:"
    ls /var/lib/lucene
else
    echo "ERROR: /var/lib/lucene/ directory does not contain any index!!!!!"
    rm -rf ${TMP_DIR}
    exit 1
fi


# print out status message
echo
echo
echo "Finished checking Project 3A submission"
echo "Please check the output of this script to ensure a proper submission"

# clean temporary files used for grading
rm -rf ${TMP_DIR}

exit 0
