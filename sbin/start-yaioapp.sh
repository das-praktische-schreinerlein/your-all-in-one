#!/usr/bin/env bash
# <h4>FeatureDomain:</h4>
#     Collaboration
# <h4>FeatureDescription:</h4>
#     run the app with rest-services
# <h4>Syntax:</h4>
#     PROG
# <h4>Example:</h4>
#     cd /cygdrive/D/public_projects/yaio/yaio
#     sbin/start-yaioapp
# 
# @package de.yaio
# @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
# @category Collaboration
# @copyright Copyright (c) 2011-2014, Michael Schreiner
# @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0

# set pathes
YAIOSCRIPTPATH=$(dirname $0)/
YAIOBASEPATH=${YAIOSCRIPTPATH}
BASEPATH=${YAIOBASEPATH}
YAIOCONFIGPATH=${YAIOSCRIPTPATH}../config/

# init config
. ${YAIOCONFIGPATH}/config-server.sh ${YAIOSCRIPTPATH}

CMD="java ${JAVAOPTIONS} -cp ${CP} ${PROG_FLYWAY} ${FLYWAYCFGFILE}"
echo "run-flyway: ${CMD}"
${CMD}

# add --debug option to see the startprocess of spring-boot
CMD="java ${JAVAOPTIONS} -cp ${CP} ${PROG_APP} ${CFG} ${NEWID_OPTIONS}"
echo "start-yaioapp: ${CMD}"
${CMD} &

CMD="java ${JAVAOPTIONS} -cp ${CP} ${PROG_APPPROPAGATOR} ${CFG}"
echo "start-apppropagator: ${CMD}"
${CMD} &
