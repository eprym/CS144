#!/bin/bash
GRADING_DIR=$HOME/grading
TMP_DIR=/tmp/p2-grading/
REQUIRED_FILES="team.txt css-layout.html create.sql drop.sql load.sql queries.sql runLoad.sh build.xml README.txt"

# usage
if [ $# -ne 1 ]
then
     echo "Usage: $0 project2.zip" 1>&2
     exit
fi

# make sure that the script runs on VM
if [ `hostname` != "class-vm" ]; then
     echo "ERROR: You need to run this script within the class virtual machine" 1>&2
     exit
fi

ZIP_FILE=$1

# clean any existing files
rm -rf ${TMP_DIR}

# create temporary directory used for grading
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
JAVA_FILES=`find src -name '*.java' -print`
if [ -z "${JAVA_FILES}" ]; then
    echo "ERROR: No java file is included in src folder of your zip file" 1>&2
    rm -rf ${TMP_DIR}
    exit 1
fi

# drop and re-create CS144 database to make sure a clean start
echo "DROP DATABASE IF EXISTS CS144; CREATE DATABASE CS144;" | mysql -uroot -ppassword

# parse the XML data and run the load script
echo "Running your runLoad.sh script..."
./runLoad.sh

# run the queries
echo "Running your query.sql script..."
mysql CS144 < queries.sql

# echo reminder message
echo
echo
echo "Finished running your queries."
echo "Please make sure that you got correct results from your queries"

# clean up
rm -rf ${TMP_DIR}
exit 0
