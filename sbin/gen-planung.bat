echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     parse and generate all from an existing wiki-file
rem <h4>Syntax:</h4>
rem     PROG projektpath filename_without_extension projectname
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\gen-planung.bat src\test\testproject\ test Testprojekt
rem 
rem @package de.yaio
rem @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
rem @category Collaboration
rem @copyright Copyright (c) 2011-2014, Michael Schreiner
rem @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0

rem set CONFIG
set MMPATH=%1%
set SRCFILE=%2%
set PROJNAME=%3%
set FLGWP=%4%
set SYSUID=%5%


rem set pathes
set YAIOSCRIPTPATH=%~dp0
set YAIOBASEPATH=%YAIOSCRIPTPATH%
set BASEPATH=%YAIOBASEPATH%
set YAIOCONFIGPATH=%YAIOSCRIPTPATH%..\config\

rem init config
call %YAIOCONFIGPATH%\config-cli.bat %YAIOSCRIPTPATH%

rem 
rem parse from wiki
rem 
if "%FLG_PARSE_PPL_FROM_WIKI%" EQU "" goto end_PARSE_PPL_FROM_WIKI

:do_PARSE_PPL_FROM_WIKI
set SRC_OPTIONS=--sourcetype ppl

rem only if html-Src: 
call %YAIOSCRIPTPATH%genPPLFromWiki.bat %MMPATH% %SRCFILE% %PROJNAME%
rem only if Excel-Src: call %YAIOSCRIPTPATH%genPPLFromExcel.bat %MMPATH% %SRCFILE% %PROJNAME%
rem gen wiki with initial recalc to get SysUID... for new items from ppl
set OUTPUT_OPTIONS_WIKI=%OUTPUT_OPTIONS_WIKI% -recalc
call %YAIOSCRIPTPATH%genWikiFromPPL.bat %MMPATH% %SRCFILE% %PROJNAME%
:end_PARSE_PPL_FROM_WIKI

rem 
rem import ppl to db
rem 
if "%FLG_IMPORT_PPL_TO_DB%" EQU "" goto end_IMPORT_PPL_TO_DB
set SRC_OPTIONS=--sourcetype ppl
call %BASEPATH%importJPAFromPPL.bat %MMPATH% %SRCFILE% %PROJNAME%
:end_IMPORT_PPL_TO_DB


rem 
rem other exports
rem 
if NOT "%PARSEONLY%" EQU "" goto end

if "%GEN_SRC%" EQU "ppl" goto configure_ppl
if "%GEN_SRC%" EQU "jpa" goto configure_jpa

echo "unknown GEN_SRC:%GEN_SRC%";
halt;
goto end

rem configure jpl
:configure_ppl
set SRC_OPTIONS=--sourcetype ppl
goto otherexports

rem jpa
:configure_jpa
set SRC_OPTIONS=--sourcetype jpa --exportsysuid %SYSUID%
goto otherexports

rem generate all other
:otherexports
call %YAIOSCRIPTPATH%genWikiFromPPL.bat %MMPATH% %SRCFILE% %PROJNAME%
call %YAIOSCRIPTPATH%genJSONFromPPL.bat %MMPATH% %SRCFILE% %PROJNAME%
call %YAIOSCRIPTPATH%genCsvFromPPL.bat %MMPATH% %SRCFILE% %PROJNAME%
call %YAIOSCRIPTPATH%genHtmlFromPPL.bat %MMPATH% %SRCFILE% %PROJNAME%
call %YAIOSCRIPTPATH%genMMFromPPL.bat %MMPATH% %SRCFILE% %PROJNAME%
call %YAIOSCRIPTPATH%genICalFromPPL.bat %MMPATH% %SRCFILE% %PROJNAME%
call %YAIOSCRIPTPATH%genExcelFromPPL.bat %MMPATH% %SRCFILE% %PROJNAME%

:4extern
set OUTPUT_OPTIONS_OLD=%OUTPUT_OPTIONS%
set OUTPUT_OPTIONS=-s -intend 80 
set EXTERN_SRCFILE=%SRCFILE%-4extern
copy %MMPATH%%SRCFILE%.ppl %MMPATH%%EXTERN_SRCFILE%.ppl
call %YAIOSCRIPTPATH%genWikiFromPPL.bat %MMPATH% %EXTERN_SRCFILE% %PROJNAME%
call %YAIOSCRIPTPATH%genHtmlFromPPL.bat %MMPATH% %EXTERN_SRCFILE% %PROJNAME%
set OUTPUT_OPTIONS=%OUTPUT_OPTIONS_OLD%

:end
rem Bye