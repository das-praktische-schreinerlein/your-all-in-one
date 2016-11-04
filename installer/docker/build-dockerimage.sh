#!/bin/bash

# check yaio_srcdir
BASE_DIR=$(dirname $(readlink -f $0))/
YAIO_SRC_BASE=${BASE_DIR}/../../

ME=`basename "$0"`
PWD=`pwd`

###################
## extract yaio-version
###################
cd ${YAIO_SRC_BASE}
YAIO_VERSION=`printf 'VERSION=${project.version}\n0\n' | \
              mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate | \
              grep '^VERSION' | \
              sed -e 's/VERSION=\(.*\)/\1/'`
echo "$ME: extracted YAIO-version: ${YAIO_VERSION}"

###################
## Build yaio
###################
cd ${BASE_DIR}
echo "$ME: start setup-build for ${YAIO_SRC_BASE}"
${BASE_DIR}/setup-build.sh ${YAIO_SRC_BASE}

###################
## Configure docker
###################
DOCKER_SRC_DIR=./yaio/
BASE_TAG=yaio/yaioserver
BASE_VERSION=`date +%Y%m%d_%H%M%S`
VERSION_TAGS=""
#VERSION_TAGS="${VERSION_TAGS} -t ${BASE_TAG}:${BASE_VERSION}"
VERSION_TAGS="${VERSION_TAGS} -t ${BASE_TAG}:${YAIO_VERSION}"

###################
## Build docker
###################
cd ${BASE_DIR}
echo "$ME: create image: ${VERSION_TAGS}"
RUN_CMD="docker build -f ${DOCKER_SRC_DIR}/Dockerfile ${VERSION_TAGS} ${DOCKER_SRC_DIR}/"
echo "$ME:do ${RUN_CMD}"
${RUN_CMD}

###################
## Back
###################
echo "$ME: finished"
cd ${PWD}
