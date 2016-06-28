echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     parse and generate late-, deadline- and todo-exports from an existing wiki-file
rem <h4>Syntax:</h4>
rem     PROG projektpath filename_without_extension projectname
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\gen-planung-todo.bat src\test\testproject\ test Testprojekt
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
call %YAIOCONFIGPATH%\config-cli.bat %BASEPATH%

rem save old outputoptions
set OUTPUT_OPTIONS_OLD=%OUTPUT_OPTIONS%

rem generate late-todos
set OUTPUT_OPTIONS=%OUTPUT_OPTIONS_OLD% -onlyifstateinlist LATE,WARNING,EVENT_CONFIRMED,EVENT_LATE,EVENT_SHORT
copy %MYPLANDIR%%MYPLANFILEBASE%.ppl %MYPLANDIR%%MYPLANFILEBASE%-late.ppl
call %BASEPATH%genICalFromPPL.bat %MYPLANDIR% %MYPLANFILEBASE%-late %MYPLANNAME%
call %BASEPATH%genHtmlFromPPL.bat %MYPLANDIR% %MYPLANFILEBASE%-late %MYPLANNAME%

rem generate deadline-todos
set OUTPUT_OPTIONS=%OUTPUT_OPTIONS_OLD% -onlyifstateinlist LATE,WARNING,RUNNING,EVENT_CONFIRMED,EVENT_LATE,EVENT_SHORT
copy %MYPLANDIR%%MYPLANFILEBASE%.ppl %MYPLANDIR%%MYPLANFILEBASE%-deadline.ppl
call %BASEPATH%genICalFromPPL.bat %MYPLANDIR% %MYPLANFILEBASE%-deadline %MYPLANNAME%
call %BASEPATH%genHtmlFromPPL.bat %MYPLANDIR% %MYPLANFILEBASE%-deadline %MYPLANNAME%

rem generate all-todos
set OUTPUT_OPTIONS=%OUTPUT_OPTIONS_OLD% -onlyifstateinlist OFFEN,OPEN,LATE,WARNING,RUNNING,EVENT_CONFIRMED,EVENT_LATE,EVENT_SHORT
copy %MYPLANDIR%%MYPLANFILEBASE%.ppl %MYPLANDIR%%MYPLANFILEBASE%-todo.ppl
call %BASEPATH%genICalFromPPL.bat %MYPLANDIR% %MYPLANFILEBASE%-todo %MYPLANNAME%
call %BASEPATH%genHtmlFromPPL.bat %MYPLANDIR% %MYPLANFILEBASE%-todo %MYPLANNAME%

rem restore old outputoptions
set OUTPUT_OPTIONS=%OUTPUT_OPTIONS_OLD%
 
