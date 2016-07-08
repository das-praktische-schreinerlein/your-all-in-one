#!/bin/bash
# <h4>FeatureDomain:</h4>
#     Collaboration
# <h4>FeatureDescription:</h4>
#     stop the app with rest-services
# <h4>Syntax:</h4>
#     PROG
# <h4>Example:</h4>
#     cd /cygdrive/D/public_projects/yaio/yaio
#     sbin/stop-yaioapp
# 
# @package de.yaio
# @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
# @category Collaboration
# @copyright Copyright (c) 2011-2014, Michael Schreiner
# @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0

# set pathes
YAIOSCRIPTPATH=$(dirname $(readlink -f $0))/
YAIOBASEPATH=${YAIOSCRIPTPATH}
BASEPATH=${YAIOBASEPATH}
YAIOCONFIGPATH=${YAIOSCRIPTPATH}../config/

# init config
. ${YAIOCONFIGPATH}/config-server.sh ${YAIOSCRIPTPATH}

# add --debug option to see the startprocess of spring-boot
CMD="java ${JAVAOPTIONS} -cp \"${CP}\" ${PROG_APP} ${CFG} ${NEWID_OPTIONS}"
echo "stop-yaioapp: ${CMD}"
pid=`ps aux | grep "${CMD}" | grep -v grep | awk '{print $2}'`
echo "kill $pid"
kill TERM $pid


CMD="java ${JAVAOPTIONS} -cp \"${CP}\" ${PROG_APPPROPAGATOR} ${CFG}"
echo "stop-apppropagator: ${CMD}"
pid=`ps aux | grep "${CMD}" | grep -v grep | awk '{print $2}'`
echo "kill $pid"
kill TERM $pid

