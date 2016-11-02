#!/bin/bash
###################
## install yaio-demoapp
###################

export SCRIPT_DIR=$(dirname $0)/
export LINUX_BASE_DIR=${SCRIPT_DIR}/../
export BASE_DIR=${LINUX_BASE_DIR}/../../
ME=`basename "$0"`

chmod 755 -R ${LINUX_BASE_DIR}/*.sh

# install
echo "$ME: start full build"
${SCRIPT_DIR}/basecommon-prepare-base-packages.sh
${SCRIPT_DIR}/build-prepare-packages.sh
${SCRIPT_DIR}/install-prepare-packages.sh
${SCRIPT_DIR}/build-yaio.sh


