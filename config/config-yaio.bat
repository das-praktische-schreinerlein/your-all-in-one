echo off

rem set CONFIG
set BASEPATH=%1%
set FLGWP=%2%

rem set appconfig
set STARTURL=http://localhost:8083/yaio-explorerapp/yaio-explorerapp.html#/

rem Gen Wiki-Only
set PARSEONLY=
rem parse ppl from wiki an generate 
set FLG_PARSE_PPL_FROM_WIKI=
rem import the ppl to db
set FLG_IMPORT_PPL_TO_DB=
rem the generate src: ppl or jpa
set GEN_SRC=jpa
rem the masternode
set MASTERNODEID=MasterplanMasternode1
set IMPORT_OPTIONS_JPA=--addnodestosysuid %MASTERNODEID%

rem set Path
set YAIOCONFIGPATH=%BASEPATH%\..\config\
set YAIOSCRIPTPATH=%BASEPATH%\..\sbin\
set YAIORESPATH=%BASEPATH%\..\ressources\
set YAIOVARPATH=%BASEPATH%\..\var\
rem old set YAIOAPP=%BASEPATH%..\target\yaio-0.1.0.BUILD-SNAPSHOT-jar-with-dependencies.jar
rem fast set YAIOAPP=%BASEPATH%..\target\classes\;%BASEPATH%..\target\yaio-0.1.0.BUILD-SNAPSHOT-shaded.jar
rem old prod set YAIOAPP=%BASEPATH%..\target\yaio-0.1.0.BUILD-SNAPSHOT-shaded.jar
rem new prod
set YAIOAPP=%BASEPATH%..\target\yaio-0.1.0.BUILD-SNAPSHOT-shaded.jar
set CP="%YAIOAPP%;"
set CFGFILE=%BASEPATH%..\config\application.properties
set CFG=--config %CFGFILE% 
set JAVAOPTIONS=-Xmx768m -Xms128m -Dspring.config.location=file:%CFGFILE% -Dlog4j.configuration=file:%BASEPATH%..\config\log4j.properties


rem change CodePage
CHCP 1252

rem set progs
set PROG_WP=de.yaio.extension.datatransfer.wiki.JobParseWiki
set PROG_WN=de.yaio.extension.datatransfer.wiki.JobNodes2Wiki
set PROG_EX=de.yaio.extension.datatransfer.excel.JobNodes2Excel
set PROG_EP=de.yaio.extension.datatransfer.excel.JobParseExcel
set PROG_HN=de.yaio.extension.datatransfer.html.JobNodes2Html
set PROG_MM=de.yaio.extension.datatransfer.mindmap.JobNodes2MindMap
set PROG_ICalN=de.yaio.extension.datatransfer.ical.JobNodes2ICal
set PROG_CSVN=de.yaio.extension.datatransfer.csv.JobNodes2CSV
set PROG_JSONN=de.yaio.extension.datatransfer.json.JobNodes2JSON
set PROG_JPAN=de.yaio.extension.datatransfer.jpa.JobNodes2JPA
set PROG_APP=de.yaio.app.Application
set PROG_RECALC=de.yaio.jobs.JobRecalcNodes
set PROG_DIFF=
set PROG_WINMERGE="C:\ProgrammePortable\PortableApps\PortableApps\WinMergePortable\WinMergePortable.exe"

set FILE_DELIM=-

echo %CP%

rem set Config
set NEWID_OPTIONS=-pathiddb %YAIOVARPATH%\\nodeids.properties
set PARSER_OPTIONS=%NEWID_OPTIONS% %FLGWP%
set PARSER_OPTIONS_EXCEL=%NEWID_OPTIONS%
set OUTPUT_OPTIONS=--calcsum -U 3 -p -i -s -d -t -intend 80 -intendli 2 -l 
rem normal (recalcsum)
set OUTPUT_OPTIONS_WIKI= -U 3 -p -i -s -d -t -intend 80 -intendli 2 -l 
rem with calcsum set OUTPUT_OPTIONS_WIKI=--calcsum -U 3 -p -i -s -d -t -intend 80 -intendli 2 -l 
rem set OUTPUT_OPTIONS=--calcsum -U 3 -p -i -s -d -t -intend 80 -intendli 2 -l -shownometadata -shownosysdata

