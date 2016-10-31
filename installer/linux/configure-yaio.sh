#!/bin/bash
###################
## Configure and package yaio
###################

SCRIPT_DIR=$(dirname $(readlink -f $0))/
BASE_DIR=${SCRIPT_DIR}/../../
CONFIG_DIR=${BASE_DIR}/config/

# load utils
. ${SCRIPT_DIR}/utils.sh

# Config anpassen

# config/application.properties
replaceConfigFileBySed ${CONFIG_DIR}/yaio-application.properties ${SCRIPT_DIR}/application.properties.sed

# config/log4j.properties
replaceConfigFileBySed ${CONFIG_DIR}/log4j.properties ${SCRIPT_DIR}/log4j.properties.sed
