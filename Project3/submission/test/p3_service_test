#!/bin/bash
GRADING_DIR=$HOME/grading
TMP_DIR=/tmp/p3c-grading/
REQUIRED_FILES="build.xml README.txt"
AAR_FILE="build/AuctionSearchService.aar"

# usage
if [ $# -ne 1 ]
then
     echo "Usage: $0 project3-searcher.zip" 1>&2
     exit 1
fi
ZIP_FILE=$1

# make sure that the script runs on VM
if [ `hostname` != "class-vm" ]; then
     echo "ERROR: You need to run this script within the class virtual machine" 1>&2
     exit 1
fi

# make sure that a lucene index exists in /var/lib/lucene directory
if [[ -n $(ls) ]]; then
    :
else
    echo "ERROR: /var/lib/lucene/ does not contain any lucene index!!!!!"
    exit 1
fi

# clean any existing files
rm -rf ${TMP_DIR}
rm -rf ${CATALINA_BASE}/webapps/axis2/WEB-INF/services/AuctionSearchService*

# create temporary directory used for grading
mkdir ${TMP_DIR}

#
# check Part C submission
#

# unzip the partc zip file
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

# change directory to the partc folder
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

# stop tomcat
#echo "Stopping tomcat server if running..."
#wget --quiet --no-clobber http://localhost:8080 && $CATALINA_HOME/bin/shutdown.sh

# run ant script to build your AuctionSearch service
echo "Running 'ant build' to build your AuctionSearch service..."
ant build

# check if the .aar file has been built
if [ ! -f ${AAR_FILE} ]; then
    echo "ERROR: Cannot find ${AAR_FILE} after 'ant build'" 1>&2
    rm -rf ${TMP_DIR}
    exit 1
fi

# deploy new tomcat
echo "Deploying your AuctionSearchService.aar..."
cp -f ${AAR_FILE} ${CATALINA_BASE}/webapps/axis2/WEB-INF/services

# start tomcat
#echo "Starting tomcat server..."
#cd $CATALINA_HOME/bin
#./startup.sh

# clean up
rm -rf ${TMP_DIR}

#
# test with the demo client
#

# create temporary directory for client testing
mkdir ${TMP_DIR}

# unzip the partd zip file
cd ${TMP_DIR}
wget http://oak.cs.ucla.edu/cs144/projects/project3/project3-client.zip
if [ "$?" -ne "0" ]; then 
    echo "ERROR: Cannot download SOAP client code" 1>&2
    rm -rf ${TMP_DIR}
    exit 1
fi
unzip -q -d ${TMP_DIR} project3-client.zip
if [ "$?" -ne "0" ]; then 
    echo "ERROR: Cannot unzip SOAP client code to ${TMP_DIR}"
    rm -rf ${TMP_DIR}
    exit 1
fi


# build service client
echo "Compiling AuctionSearch service client..."
ant compile

# run service client
echo "Waiting for 10 seconds until AuctionSearchService is reloaded..."
sleep 10
echo "Running AuctionSearch service client..."
ant run

# stop tomcat
#echo "Stopping tomcat server..."
#cd $CATALINA_HOME/bin
#./shutdown.sh

# clean up
rm -rf ${TMP_DIR}

# print out message
echo
echo
echo "Finished checking your Project 3 submission"
echo "Please check the output of this script to ensure a working submission"
exit 0
