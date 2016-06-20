#!/usr/bin/env bash
#/!/bin/bash
echo off

# set CONFIG
export BASEPATH=${1}
export FLGWP=${2}

# set pathes
export GRAPHVIZ_DOT=/usr/bin/dot

# set appconfig
export YAIOINSTANCE=yaio-playground.local
export YAIOAPPURLCONFIG=-config dummy -yaioinstance ${YAIOINSTANCE} -username admin -password secret
export STARTURL=http://${YAIOINSTANCE}/index.html

# Gen Wiki-Only
export PARSEONLY=
# parse ppl from wiki an generate 
export FLG_PARSE_PPL_FROM_WIKI=
# import the ppl to db
export FLG_IMPORT_PPL_TO_DB=
# the generate src: ppl or jpa
export GEN_SRC=jpa
# the masternode
export MASTERNODEID="MasterplanMasternode1"
export IMPORT_OPTIONS_JPA="--addnodestosysuid ${MASTERNODEID}"

# set Path
export YAIOCONFIGPATH=${BASEPATH}/../config/
export YAIOSCRIPTPATH=${BASEPATH}/../sbin/
export YAIORESPATH=${BASEPATH}/../resources/
export YAIOVARPATH=${BASEPATH}/../var/
export YAIOAPP=${BASEPATH}../target/yaio.jar
export APPPROPAGATOR=${BASEPATH}../sbin/apppropagator.jar
export CP="${YAIOAPP}:${APPPROPAGATOR}:${BASEPATH}../target/"
export CFGFILE=${BASEPATH}../config/application.properties
export CFG="--config ${CFGFILE}"
export FLYWAYCFG=${CFG}
export JAVAOPTIONS="-Xmx768m -Xms128m -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses -Dspring.config.location=file:${CFGFILE} -Dlog4j.configuration=file:${BASEPATH}../config/log4j.properties"


# set progs
export PROG_WP=de.yaio.app.extension.datatransfer.wiki.JobParseWiki
export PROG_WN=de.yaio.app.extension.datatransfer.wiki.JobNodes2Wiki
export PROG_EX=de.yaio.app.extension.datatransfer.excel.JobNodes2Excel
export PROG_EP=de.yaio.app.extension.datatransfer.excel.JobParseExcel
export PROG_HN=de.yaio.app.extension.datatransfer.html.JobNodes2Html
export PROG_MM=de.yaio.app.extension.datatransfer.mindmap.JobNodes2MindMap
export PROG_ICalN=de.yaio.app.extension.datatransfer.ical.JobNodes2ICal
export PROG_CSVN=de.yaio.app.extension.datatransfer.csv.JobNodes2CSV
export PROG_JSONN=de.yaio.app.extension.datatransfer.json.JobNodes2JSON
export PROG_JPAN=de.yaio.app.extension.datatransfer.jpa.JobNodes2JPA
export PROG_CALLYAIORESET=de.yaio.app.jobs.clients.CallYaioReset
export PROG_CALLYAIORECALC=de.yaio.app.jobs.clients.CallYaioRecalc
export PROG_CALLYAIOEXPORT=de.yaio.app.jobs.clients.CallYaioExport
export PROG_CALLYAIOIMPORT=de.yaio.app.jobs.clients.CallYaioImport
export PROG_APP=de.yaio.app.webapp.Application
export PROG_APPPROPAGATOR=de.yitf.app.apppropagator.UpnpAppPropagator
export PROG_RECALC=de.yaio.app.jobs.batch.JobRecalcNodes
export PROG_FLYWAY=de.yaio.app.jobs.batch.JobYaioFlyway
export PROG_DIFF=
export PROG_WINMERGE="C:/ProgrammePortable/PortableApps/PortableApps/WinMergePortable/WinMergePortable.exe"

export FILE_DELIM="-"

echo ${CP}

# set Config
export NEWID_OPTIONS="-pathiddb ${YAIOVARPATH}//nodeids.properties"
export PARSER_OPTIONS="${NEWID_OPTIONS} ${FLGWP}"
export PARSER_OPTIONS_EXCEL="${NEWID_OPTIONS}"
export OUTPUT_OPTIONS="--calcsum -U 3 -p -i -s -d -t -intend 80 -intendli 2 -l" 
# normal (recalcsum)
export OUTPUT_OPTIONS_WIKI=" -U 3 -p -i -s -d -t -intend 80 -intendli 2 -l" 
# with calcsum export OUTPUT_OPTIONS_WIKI="--calcsum -U 3 -p -i -s -d -t -intend 80 -intendli 2 -l" 
# export OUTPUT_OPTIONS="--calcsum -U 3 -p -i -s -d -t -intend 80 -intendli 2 -l -shownometadata -shownosysdata"

