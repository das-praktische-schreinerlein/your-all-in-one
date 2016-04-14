#!/bin/bash
# gen changelog.wiki

# load utils
source utils.bash

LOGFROM=2016-03-05
LOGFOR=release-201604

YAIO_DIR=/cygdrive/d/Projekte/yaio-playground
YAIOAPP_DIR=/cygdrive/d/Projekte/yaio-explorerapp

YAIO_FILEBASE=$YAIO_DIR/resources/docs/yaio
YAIOAPP_FILEBASE=$YAIO_DIR/resources/docs/yaio-explorerapp

SCRIPTPATH=$( cd $(dirname $0) ; pwd -P )

# create tpl
cd $YAIO_DIR
git log --since=$LOGFROM --date=short --reverse --format="*** ERLEDIGT - %s [Ist: 100%% 1h %ad-%ad] [Plan: 1h %ad-%ad] [NodeMeta: ,,,TaskNodeMetaNodeSubType.TASK]\n# Aufgabe\n- %s\n\n## Stand\n- [ERLEDIGT] - Konzept\n- [ERLEDIGT] - Umsetzung\n\n## Konzept\n" > $YAIO_FILEBASE-gitlog.tmp
cat $YAIO_FILEBASE-gitlog.tmp | sed -r -f $SCRIPTPATH/gen-changelog.sed > $YAIO_FILEBASE-gitchangelog-$LOGFOR.wiki

cd $YAIOAPP_DIR
git log --since=$LOGFROM --date=short --reverse --format="*** ERLEDIGT - %s [Ist: 100%% 1h %ad-%ad] [Plan: 1h %ad-%ad] [NodeMeta: ,,,TaskNodeMetaNodeSubType.TASK]\n# Aufgabe\n- %s\n\n## Stand\n- [ERLEDIGT] - Konzept\n- [ERLEDIGT] - Umsetzung\n\n## Konzept\n" > $YAIOAPP_FILEBASE-gitlog.tmp
cat $YAIOAPP_FILEBASE-gitlog.tmp | sed -r -f $SCRIPTPATH/gen-changelog.sed > $YAIOAPP_FILEBASE-gitchangelog-$LOGFOR.wiki

cd $SCRIPTPATH