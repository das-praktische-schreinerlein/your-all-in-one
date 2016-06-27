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
export STARTURL=http://${YAIOINSTANCE}/index.html

# set Path
export YAIOCONFIGPATH=${BASEPATH}/../config/
export YAIOSCRIPTPATH=${BASEPATH}/../sbin/
export YAIORESPATH=${BASEPATH}/../resources/
export YAIOVARPATH=${BASEPATH}/../var/
export YAIOAPP=${BASEPATH}../dist/yaio-app-server-full.jar
export APPPROPAGATOR=${BASEPATH}../sbin/apppropagator.jar
export CP="${YAIOAPP}:${APPPROPAGATOR}:${BASEPATH}../target/"
export CFGFILE=${BASEPATH}../config/yaio-application.properties
export CFG="--config ${CFGFILE}"
export FLYWAYCFG=${CFG}
export JAVAOPTIONS="-Xmx768m -Xms128m -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses -Dspring.config.location=file:${CFGFILE} -Dlog4j.configuration=file:${BASEPATH}../config/log4j.properties"


# set progs
export PROG_APP=de.yaio.app.server.Application
export PROG_APPPROPAGATOR=de.yitf.app.apppropagator.UpnpAppPropagator
export PROG_FLYWAY=de.yaio.app.system.JobYaioFlyway
export PROG_DIFF=
export PROG_WINMERGE="C:/ProgrammePortable/PortableApps/PortableApps/WinMergePortable/WinMergePortable.exe"

export FILE_DELIM="-"

echo ${CP}
