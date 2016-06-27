#!/usr/bin/env bash
#/!/bin/bash
echo off

# set CONFIG
export BASEPATH=${1}
export FLGWP=${2}

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
export YAIOAPP=${BASEPATH}../target/yaio-app-cli-full.jar
export CP="${YAIOAPP}"
export CFGFILE=${BASEPATH}../config/yaio-application.properties
export CFG="--config ${CFGFILE}"
export FLYWAYCFG=${CFG}
export JAVAOPTIONS="-Xmx768m -Xms128m -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses -Dspring.config.location=file:${CFGFILE} -Dlog4j.configuration=file:${BASEPATH}../config/log4j.properties"


# set progs
export PROG_WP=de.yaio.app.cli.importer.JobParseWiki
export PROG_WN=de.yaio.app.cli.converter.JobNodes2Wiki
export PROG_EX=de.yaio.app.cli.converter.JobNodes2Excel
export PROG_EP=de.yaio.app.cli.importer.JobParseExcel
export PROG_HN=de.yaio.app.cli.converter.JobNodes2Html
export PROG_MM=de.yaio.app.cli.converter.JobNodes2MindMap
export PROG_ICalN=de.yaio.app.cli.converter.JobNodes2ICal
export PROG_CSVN=de.yaio.app.cli.converter.JobNodes2CSV
export PROG_JSONN=de.yaio.app.cli.converter.JobNodes2JSON
export PROG_JPAN=de.yaio.app.cli.converter.JobNodes2JPA
export PROG_RECALC=de.yaio.app.cli.batch.JobRecalcNodes
export PROG_FLYWAY=de.yaio.app.system.JobYaioFlyway
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

