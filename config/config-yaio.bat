echo off

rem set CONFIG
set BASEPATH=%1%
set FLGWP=%2%

rem Gen Wiki-Only
set WIKIONLY=

rem set Path
set YAIOCONFIGPATH=%BASEPATH%\..\config\
set YAIOSCRIPTPATH=%BASEPATH%\..\sbin\
set YAIORESPATH=%BASEPATH%\..\ressources\
set YAIOVARPATH=%BASEPATH%\..\var\
set CP="%BASEPATH%..\lib\poi-extended.jar;%BASEPATH%..\lib\commons-cli-1.2.jar;%BASEPATH%..\target\yaio-0.1.0.BUILD-SNAPSHOT-jar-with-dependencies.jar;"
set JAVAOPTIONS=-Xmx512m -Xms128m -Dlog4j.configuration=file:%BASEPATH%..\config\log4j.properties
set CFG="%BASEPATH%..\config"


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
set PROG_DIFF=
set PROG_WINMERGE="C:\ProgrammePortable\PortableApps\PortableApps\WinMergePortable\WinMergePortable.exe"

set FILE_DELIM=-

echo %CP%

rem set Config
set PARSER_OPTIONS=-pathiddb %YAIOVARPATH%\nodeids.properties %FLGWP%
set PARSER_OPTIONS_EXCEL=-pathiddb %YAIOVARPATH%\\nodeids.properties
set OUTPUT_OPTIONS=-c -U 3 -p -i -s -d -t -intend 80 -intendli 2 -l 
set OUTPUT_OPTIONS_WIKI=-U 3 -p -i -s -d -t -intend 80 -intendli 2 -l 
rem set OUTPUT_OPTIONS=-c -U 3 -p -i -s -d -t -intend 80 -intendli 2 -l -shownometadata -shownosysdata

