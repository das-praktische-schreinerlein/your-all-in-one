#!/bin/bash
# usage: setup-distribution.sh /cygdrive/d/Projekte/yaio-playground/build

# check YAIO_DIST_DIR
YAIO_DIST_DIR=$1

if [ "${YAIO_DIST_DIR}" = "" ]; then
   echo "YAIO_DIST_DIR required - for instance"
   echo "   ./setup-distribution.sh /cygdrive/d/Projekte/yaio-playground/build"
   exit -1
fi

SCRIPT_DIR=$(dirname $(readlink -f $0))/
BASE_DIR=${SCRIPT_DIR}/../../
CONFIG_DIR=${BASE_DIR}/config/
ME=`basename "$0"`

###################
## Configure and package yaio
###################
YAIO_SRC_BASE=${BASE_DIR}/

# prepare build-dir
echo "$ME: create dist-dirs: ${YAIO_DIST_DIR}"
rm -fr ${YAIO_DIST_DIR}
mkdir -p ${YAIO_DIST_DIR}


# create empty dirs
mkdir -p ${YAIO_DIST_DIR}/config
mkdir -p ${YAIO_DIST_DIR}/dist
mkdir -p ${YAIO_DIST_DIR}/installer/linux
mkdir -p ${YAIO_DIST_DIR}/logs
mkdir -p ${YAIO_DIST_DIR}/queues/fileimportqueue
mkdir -p ${YAIO_DIST_DIR}/queues/mailimportqueue
mkdir -p ${YAIO_DIST_DIR}/resources
mkdir -p ${YAIO_DIST_DIR}/sbin
mkdir -p ${YAIO_DIST_DIR}/storage
mkdir -p ${YAIO_DIST_DIR}/tmp
mkdir -p ${YAIO_DIST_DIR}/var/hsqldb
mkdir -p ${YAIO_DIST_DIR}/var/tessdata

# copy data
echo "$ME: create copy data from $YAIO_SRC_BASE -> ${YAIO_DIST_DIR}"
cp -r ${YAIO_SRC_BASE}/config/* ${YAIO_DIST_DIR}/config/
cp -r ${YAIO_SRC_BASE}/installer/linux/* ${YAIO_DIST_DIR}/installer/linux/
#cp -r ${YAIO_SRC_BASE}/dist/yaio-app-cli-full.jar ${YAIO_DIST_DIR}/dist/
cp -r ${YAIO_SRC_BASE}/dist/yaio-app-server-full.jar ${YAIO_DIST_DIR}/dist/
#cp -r ${YAIO_SRC_BASE}/dist/yaio-app-serverclient-full.jar ${YAIO_DIST_DIR}/dist/
cp -r ${YAIO_SRC_BASE}/sbin/* ${YAIO_DIST_DIR}/sbin/
cp -r ${YAIO_SRC_BASE}/resources/* ${YAIO_DIST_DIR}/resources/
cp -r ${YAIO_SRC_BASE}/var/nodeids.properties ${YAIO_DIST_DIR}/var/
cp -r ${YAIO_SRC_BASE}/var/tessdata/* ${YAIO_DIST_DIR}/var/tessdata/

echo "$ME: finished"
