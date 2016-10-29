#!/bin/bash
###################
## install yaio-demoapp
###################

export YAIOUSER=yaiodemo
export SCRIPT_DIR=$(dirname $0)/
export INSTALLER_DIR=${SCRIPT_DIR}/../
export BASE_DIR=${INSTALLER_DIR}/../
export CONFIG_DIR=${BASE_DIR}/config/

CURDIR=`pwd`

cd $BASE_DIR
mkdir logs

# config/application.properties
replaceConfigFileBySed ${CONFIG_DIR}/yaio-application.properties ${INSTALLER_DIR}/application.properties.sed

# config/log4j.properties
replaceConfigFileBySed ${CONFIG_DIR}/log4j.properties ${INSTALLER_DIR}/log4j.properties.sed
cp ${CONFIG_DIR}/log4j.properties ${BASE_DIR}/src/test/java/

# generate
mvn install

cd $CURDIR


