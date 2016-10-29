#!/bin/bash
###################
## install yaio-demoapp
###################

export YAIOUSER=yaiodemo
export SCRIPT_DIR=$(dirname $0)/
export INSTALLER_DIR=${SCRIPT_DIR}/../
export BASE_DIR=${INSTALLER_DIR}/../

chmod 755 -R ${INSTALLER_DIR}/*.sh

# create user
useradd -m ${YAIOUSER}

# install
${SCRIPT_DIR}/basecommon-prepare-base-packages.sh
${SCRIPT_DIR}/build-prepare-packages.sh
${SCRIPT_DIR}/install-prepare-packages.sh
su ${YAIOUSER} -c ${SCRIPT_DIR}/build-yaio.sh


