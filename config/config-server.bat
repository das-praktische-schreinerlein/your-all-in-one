echo off
rem ##############################
rem # Configure serverbased yaio
rem ##############################

rem set CONFIG
set BASEPATH=%1%
set FLGWP=%2%

rem set pathes
set GRAPHVIZ_DOT=D:\ProgrammeShared\graphviz-2.38\bin\dot.exe

rem set appconfig
set YAIOINSTANCE=yaio-playground.local
set STARTURL=http://%YAIOINSTANCE%/index.html

rem set Path
set YAIOCONFIGPATH=%BASEPATH%\..\config\
set YAIOSCRIPTPATH=%BASEPATH%\..\sbin\
set YAIORESPATH=%BASEPATH%\..\resources\
set YAIOVARPATH=%BASEPATH%\..\var\
rem TODO
set YAIOAPP=%BASEPATH%..\dist\yaio-app-server-full.jar
set APPPROPAGATOR=%BASEPATH%..\sbin\apppropagator.jar
set CP="%YAIOAPP%;%APPPROPAGATOR%;"
set CFGFILE=%BASEPATH%..\config\yaio-application.properties
set CFG=--config %CFGFILE% 
set FLYWAYCFG=%CFG%
set JAVAOPTIONS=-Xmx768m -Xms128m -Dspring.config.location=file:%CFGFILE% -Dlog4j.configuration=file:%BASEPATH%..\config\log4j.properties

rem change CodePage
CHCP 1252

rem set progs
set PROG_APP=de.yaio.app.server.Application
set PROG_APPPROPAGATOR=de.yitf.app.apppropagator.UpnpAppPropagator
set PROG_FLYWAY=de.yaio.app.system.JobYaioFlyway

set FILE_DELIM=-

rem echo %CP%

rem set Config
set NEWID_OPTIONS=-pathiddb %YAIOVARPATH%\\nodeids.properties

