echo off
rem ##############################
rem # Configure filebased yaio
rem ##############################

rem set CONFIG
set BASEPATH=%1%
set FLGWP=%2%

rem Gen Wiki-Only
set PARSEONLY=
echo %OVERRIDE_PARSEONLY%
IF NOT [%OVERRIDE_PARSEONLY%] == [] set PARSEONLY=%OVERRIDE_PARSEONLY%
rem parse ppl from wiki an generate 
set FLG_PARSE_PPL_FROM_WIKI=
IF NOT [%OVERRIDE_FLG_PARSE_PPL_FROM_WIKI%] == [] set FLG_PARSE_PPL_FROM_WIKI=%OVERRIDE_FLG_PARSE_PPL_FROM_WIKI%
rem import the ppl to db
set FLG_IMPORT_PPL_TO_DB=
IF NOT [%OVERRIDE_FLG_IMPORT_PPL_TO_DB%] == [] set FLG_IMPORT_PPL_TO_DB=%OVERRIDE_FLG_IMPORT_PPL_TO_DB%
rem the generate src: ppl or jpa
set GEN_SRC=ppl
IF NOT [%OVERRIDE_GEN_SRC%] == [] set GEN_SRC=%OVERRIDE_GEN_SRC%
rem the masternode
set MASTERNODEID=MasterplanMasternode1
IF NOT [%OVERRIDE_MASTERNODEID%] == [] set MASTERNODEID=%OVERRIDE_MASTERNODEID%
set IMPORT_OPTIONS_JPA=--addnodestosysuid %MASTERNODEID%

rem set Path
set YAIOCONFIGPATH=%BASEPATH%\..\config\
set YAIOSCRIPTPATH=%BASEPATH%\..\sbin\
set YAIORESPATH=%BASEPATH%\..\resources\
set YAIOVARPATH=%BASEPATH%\..\var\
rem TODO
set YAIOAPP=%BASEPATH%..\dist\yaio-app-cli-full.jar
set CP="%YAIOAPP%;"
set CFGFILE=%BASEPATH%..\config\yaio-application.properties
set CFG=--config %CFGFILE% 
set FLYWAYCFG=%CFG%

set JVMMEMOPTIONS=-Xmx512m -Xms128m
rem java 1.8
rem set JVMMEMOPTIONS=-XX:MaxMetaspaceSize=512m -Xms128m
set JVMOPTIONS=%JVMMEMOPTIONS% -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark
set JAVAOPTIONS=%JVMOPTIONS% -Dspring.config.location=file:%CFGFILE% -Dlog4j.configuration=file:%BASEPATH%..\config\log4j.properties

rem change CodePage
CHCP 1252

rem set progs
set PROG_WP=de.yaio.app.cli.importer.JobParseWiki
set PROG_WN=de.yaio.app.cli.converter.JobNodes2Wiki
set PROG_EX=de.yaio.app.cli.converter.JobNodes2Excel
set PROG_EP=de.yaio.app.cli.importer.JobParseExcel
set PROG_HN=de.yaio.app.cli.converter.JobNodes2Html
set PROG_MM=de.yaio.app.cli.converter.JobNodes2MindMap
set PROG_ICalN=de.yaio.app.cli.converter.JobNodes2ICal
set PROG_CSVN=de.yaio.app.cli.converter.JobNodes2CSV
set PROG_JSONN=de.yaio.app.cli.converter.JobNodes2JSON
set PROG_JPAN=de.yaio.app.cli.converter.JobNodes2JPA
set PROG_RECALC=de.yaio.app.cli.batch.JobRecalcNodes
set PROG_FLYWAY=de.yaio.app.system.JobYaioFlyway
set PROG_DIFF=
set PROG_WINMERGE="C:\ProgrammePortable\PortableApps\PortableApps\WinMergePortable\WinMergePortable.exe"

set FILE_DELIM=-

rem echo %CP%

rem set Config
set NEWID_OPTIONS=-pathiddb %YAIOVARPATH%\\nodeids.properties
set PARSER_OPTIONS=%NEWID_OPTIONS% %FLGWP%
set PARSER_OPTIONS_EXCEL=%NEWID_OPTIONS%
set OUTPUT_OPTIONS=--calcsum -U 3 -p -i -s -d -t -intend 80 -intendli 2 -l 
rem normal (recalcsum)
set OUTPUT_OPTIONS_WIKI= -U 3 -p -i -s -d -t -intend 80 -intendli 2 -l 
rem with calcsum set OUTPUT_OPTIONS_WIKI=--calcsum -U 3 -p -i -s -d -t -intend 80 -intendli 2 -l 
rem set OUTPUT_OPTIONS=--calcsum -U 3 -p -i -s -d -t -intend 80 -intendli 2 -l -shownometadata -shownosysdata

