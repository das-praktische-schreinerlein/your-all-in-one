#!/bin/bash
###################
## prepare system for yaio-install
###################
SCRIPT_DIR=$(dirname $0)/
SCRIT_BASE_DIR=${SCRIPT_DIR}/../

# load utils
. ${SCRIT_BASE_DIR}/utils.sh

# deactivate services
service php5-fpm stop
service mysql stop
update-rc.d -f php5-fpm remove
update-rc.d -f mysql remove
