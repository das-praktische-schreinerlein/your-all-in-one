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
YAIOSCRIPTPATH=$(dirname $(readlink -f $0))/
YAIOBASEPATH=${YAIOSCRIPTPATH}
BASEPATH=${YAIOBASEPATH}
YAIOCONFIGPATH=${YAIOSCRIPTPATH}../config/

# init config
. ${YAIOCONFIGPATH}/config-server.sh ${YAIOSCRIPTPATH}
CMD="java ${JAVAOPTIONS} -cp ${CP} ${PROG_FLYWAY} ${FLYWAYCFG}"
echo "run-flyway: ${CMD}"
${CMD}

# start Xvfb if no display set
echo "Starting X virtual framebuffer (Xvfb) for webshots in background..."
if [ -z "$DISPLAY" ]; then
  export XVFB_DISPLAY=:99
  DISPLAY=$XVFB_DISPLAY
  Xvfb -ac $XVFB_DISPLAY -screen 0 1280x1024x16 &
fi

# add --debug option to see the startprocess of spring-boot
CMD="java ${JAVAOPTIONS} -cp ${CP} ${PROG_APP} ${CFG} ${NEWID_OPTIONS}"
echo "start-yaioapp: ${CMD}"
${CMD} &

CMD="java ${JAVAOPTIONS} -cp ${APPPROPAGATOR} ${PROG_APPPROPAGATOR} ${CFG}"
echo "start-apppropagator: ${CMD}"
${CMD} &
