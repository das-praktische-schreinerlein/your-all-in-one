echo off
rem ##############################
rem # Configure clientbased yaio
rem ##############################

rem set CONFIG
set BASEPATH=%1%
set FLGWP=%2%

rem set appconfig
set YAIOINSTANCE=yaio-playground.local
set YAIOAPPURLCONFIG=-config dummy -yaioinstance %YAIOINSTANCE% -username admin -password secret

rem Gen Wiki-Only
set WIKIONLY=
rem Show Diffs
set SHOWINMERGE=1
rem the masternode
set MASTERNODEID=MasterplanMasternode1
IF NOT [%OVERRIDE_MASTERNODEID%] == [] set MASTERNODEID=%OVERRIDE_MASTERNODEID%

rem set Path
set YAIOCONFIGPATH=%BASEPATH%\..\config\
set YAIOSCRIPTPATH=%BASEPATH%\..\sbin\
set YAIORESPATH=%BASEPATH%\..\resources\
set YAIOVARPATH=%BASEPATH%\..\var\
rem TODO
set YAIOAPP=%BASEPATH%..\dist\yaio-app-serverclient-full.jar
set CP="%YAIOAPP%;"
set CFGFILE=%BASEPATH%..\config\yaio-application.properties
set CFG=--config %CFGFILE% 

set JVMMEMOPTIONS=-Xmx512m -Xms128m
rem java 1.8
rem set JVMMEMOPTIONS=-XX:MaxMetaspaceSize=512m -Xms128m
set JVMOPTIONS=%JVMMEMOPTIONS% -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark
set JAVAOPTIONS=%JVMOPTIONS% -Dspring.config.location=file:%CFGFILE% -Dlog4j.configuration=file:%BASEPATH%..\config\log4j.properties

rem change CodePage
CHCP 1252

rem set progs
set PROG_CALLYAIORESET=de.yaio.app.clients.CallYaioReset
set PROG_CALLYAIORECALC=de.yaio.app.clients.CallYaioRecalcNodes
set PROG_CALLYAIOEXPORT=de.yaio.app.clients.CallYaioExport
set PROG_CALLYAIOIMPORT=de.yaio.app.clients.CallYaioImport
set PROG_DIFF=
set PROG_WINMERGE="C:\ProgrammePortable\PortableApps\PortableApps\WinMergePortable\WinMergePortable.exe"

set FILE_DELIM=-

rem echo %CP%

