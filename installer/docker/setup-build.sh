#!/bin/bash
# usage: setup-build.sh

###################
## Configure and package yaio
###################
BASE_DIR=$(dirname $(readlink -f $0))/
DOCKER_SRC_DIR=${BASE_DIR}/yaio
YAIO_BUILD_DIR=${DOCKER_SRC_DIR}/build

ME=`basename "$0"`

# prepare build-dir
echo "$ME: prepare build-dir: ${YAIO_BUILD_DIR}"
rm -fr ${YAIO_BUILD_DIR}
mkdir -p ${YAIO_BUILD_DIR}

echo "$ME: start setup-distribution for ${YAIO_BUILD_DIR}"
${BASE_DIR}/../linux/setup-distribution.sh ${YAIO_BUILD_DIR}
