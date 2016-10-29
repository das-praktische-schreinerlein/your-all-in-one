#!/bin/bash
###################
## prepare system for yaio-install
###################
SCRIPT_DIR=$(dirname $0)/
SCRIT_BASE_DIR=${SCRIPT_DIR}/../

# load utils
. ${SCRIT_BASE_DIR}/utils.sh

# set noninteractive and updatepackages
export DEBIAN_FRONTEND=noninteractive
apt-get update -q

# install java1.8
apt-cache showpkg java
apt-get install -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" openjdk-8-jdk

# cleanup
apt-get autoremove -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold"
