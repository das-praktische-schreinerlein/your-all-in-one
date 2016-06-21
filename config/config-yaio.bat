echo off

rem set CONFIG
set BASEPATH=%1%
set FLGWP=%2%

rem set pathes
set GRAPHVIZ_DOT=D:\ProgrammeShared\graphviz-2.38\bin\dot.exe

rem set appconfig
set YAIOINSTANCE=yaio-playground.local
set YAIOAPPURLCONFIG=-config dummy -yaioinstance %YAIOINSTANCE% -username admin -password secret
set STARTURL=http://%YAIOINSTANCE%/index.html

rem ##############################
rem # Configure appbased yaio
rem Gen Wiki-Only
set WIKIONLY=
rem Show Diffs
set SHOWINMERGE=1
rem ##############################


rem ##############################
rem # Configure filebased yaio
rem Gen Wiki-Only
set PARSEONLY=
rem parse ppl from wiki an generate 
set FLG_PARSE_PPL_FROM_WIKI=1
rem import the ppl to db
set FLG_IMPORT_PPL_TO_DB=
rem the generate src: ppl or jpa
set GEN_SRC=ppl
rem the masternode
set MASTERNODEID=MasterplanMasternode1
set IMPORT_OPTIONS_JPA=--addnodestosysuid %MASTERNODEID%
rem ##############################

rem set Path
set YAIOCONFIGPATH=%BASEPATH%\..\config\
set YAIOSCRIPTPATH=%BASEPATH%\..\sbin\
set YAIORESPATH=%BASEPATH%\..\resources\
set YAIOVARPATH=%BASEPATH%\..\var\
set YAIOAPP=%BASEPATH%..\target\yaio.jar
set APPPROPAGATOR=%BASEPATH%..\sbin\apppropagator.jar
set CP="%YAIOAPP%;%APPPROPAGATOR%;"
set CFGFILE=%BASEPATH%..\config\application.properties
set CFG=--config %CFGFILE% 
set FLYWAYCFG=%CFG%
set JAVAOPTIONS=-Xmx768m -Xms128m -Dspring.config.location=file:%CFGFILE% -Dlog4j.configuration=file:%BASEPATH%..\config\log4j.properties

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
set PROG_CALLYAIORESET=de.yaio.app.clients.CallYaioReset
set PROG_CALLYAIORECALC=de.yaio.app.clients.CallYaioRecalcNodes
set PROG_CALLYAIOEXPORT=de.yaio.app.clients.CallYaioExport
set PROG_CALLYAIOIMPORT=de.yaio.app.clients.CallYaioImport
set PROG_APP=de.yaio.app.server.Application
set PROG_APPPROPAGATOR=de.yitf.app.apppropagator.UpnpAppPropagator
set PROG_RECALC=de.yaio.app.jobs.batch.JobRecalcNodes
set PROG_FLYWAY=de.yaio.app.jobs.batch.JobYaioFlyway
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

