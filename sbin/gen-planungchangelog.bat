echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     parse and generate changelog- and roadmap-exports from an existing wiki-file
rem <h4>Syntax:</h4>
rem     PROG projektpath filename_without_extension projectname
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\gen-planungchangelog.bat src\test\testproject\ test Testprojekt
rem 
rem @package de.yaio
rem @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
rem @category Collaboration
rem @copyright Copyright (c) 2011-2014, Michael Schreiner
rem @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0

rem set CONFIG
set MYPLANDIR=%1%
set MYPLANFILEBASE=%2%
set MYPLANNAME=%3%
set OPTION=%4%

rem set pathes
set YAIOSCRIPTPATH=%~dp0
set YAIOBASEPATH=%YAIOSCRIPTPATH%
set YAIOCONFIGPATH=%YAIOSCRIPTPATH%..\config\
set BASEPATH=%YAIOBASEPATH%

rem init config
call %YAIOCONFIGPATH%\config-yaio.bat %BASEPATH%

rem save old outputoptions
set OUTPUT_OPTIONS_OLD=%OUTPUT_OPTIONS%

rem cipy src-files
copy %MYPLANDIR%%MYPLANFILEBASE%.ppl %MYPLANDIR%%MYPLANFILEBASE%-changelog.ppl
copy %MYPLANDIR%%MYPLANFILEBASE%.ppl %MYPLANDIR%%MYPLANFILEBASE%-roadmap.ppl

rem generate roadmap
set OUTPUT_OPTIONS=-s -intend 80 -e 5 -onlyifstateinlist OFFEN,OPEN,LATE,WARNING,RUNNING,EVENT_PLANED,EVENT_CONFIRMED,EVENT_LATE,EVENT_SHORT
call %BASEPATH%genMMFromPPL.bat %MYPLANDIR% %MYPLANFILEBASE%-roadmap %MYPLANNAME%
call %BASEPATH%genWikiFromPPL.bat %MYPLANDIR% %MYPLANFILEBASE%-roadmap %MYPLANNAME%
call %BASEPATH%genHtmlFromPPL.bat %MYPLANDIR% %MYPLANFILEBASE%-roadmap %MYPLANNAME%

rem generate changelog
set OUTPUT_OPTIONS=-s -intend 80 -e 5 -onlyifstateinlist ERLEDIGT,EVENT_ERLEDIGT
call %BASEPATH%genMMFromPPL.bat %MYPLANDIR% %MYPLANFILEBASE%-changelog %MYPLANNAME%
call %BASEPATH%genWikiFromPPL.bat %MYPLANDIR% %MYPLANFILEBASE%-changelog %MYPLANNAME%
call %BASEPATH%genHtmlFromPPL.bat %MYPLANDIR% %MYPLANFILEBASE%-changelog %MYPLANNAME%

rem restore old outputoptions
set OUTPUT_OPTIONS=%OUTPUT_OPTIONS_OLD%
