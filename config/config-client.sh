#!/usr/bin/env bash
#/!/bin/bash
echo off

# set CONFIG
export BASEPATH=${1}
export FLGWP=${2}

# set appconfig
export YAIOINSTANCE=yaio-playground.local
export YAIOAPPURLCONFIG=-config dummy -yaioinstance ${YAIOINSTANCE} -username admin -password secret

# Gen Wiki-Only
exportWIKIONLY=
# Show Diffs
export SHOWINMERGE=1
# the masternode
export MASTERNODEID=MasterplanMasternode1
IF NOT $OVERRIDE_MASTERNODEID == "" export MASTERNODEID=$OVERRIDE_MASTERNODEID

# set Path
export YAIOCONFIGPATH=${BASEPATH}/../config/
export YAIOSCRIPTPATH=${BASEPATH}/../sbin/
export YAIORESPATH=${BASEPATH}/../resources/
export YAIOVARPATH=${BASEPATH}/../var/
export YAIOAPP=${BASEPATH}../target/yaio-app-serverclient-full.jar
export CP="${YAIOAPP}:"
export CFGFILE=${BASEPATH}../config/yaio-application.properties
export CFG="--config ${CFGFILE}"
export JAVAOPTIONS="-Xmx768m -Xms128m -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses -Dspring.config.location=file:${CFGFILE} -Dlog4j.configuration=file:${BASEPATH}../config/log4j.properties"


# set progs
export PROG_CALLYAIORESET=de.yaio.app.clients.CallYaioReset
export PROG_CALLYAIORECALC=de.yaio.app.clients.CallYaioRecalcNodes
export PROG_CALLYAIOEXPORT=de.yaio.app.clients.CallYaioExport
export PROG_CALLYAIOIMPORT=de.yaio.app.clients.CallYaioImport
export PROG_DIFF=
export PROG_WINMERGE="C:/ProgrammePortable/PortableApps/PortableApps/WinMergePortable/WinMergePortable.exe"

export FILE_DELIM="-"

echo ${CP}
