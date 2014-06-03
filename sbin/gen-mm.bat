echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     generate the export-media (Wiki, Html, MM, Excel) from an existing ppl-file
rem <h4>Syntax:</h4>
rem     PROG projektpath filename_without_extension projectname
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\gen-mm.bat src\test\testproject\ test Testprojekt
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

rem set pathes
set YAIOSCRIPTPATH=%~dp0
set YAIOBASEPATH=%YAIOSCRIPTPATH%
set YAIOCONFIGPATH=%YAIOSCRIPTPATH%..\config\
set BASEPATH=%YAIOBASEPATH%

rem init config
call %YAIOCONFIGPATH%\config-yaio.bat %BASEPATH%

rem nur Plandaten (kein IST/Status) set OUTPUT_OPTIONS=-p
call %YAIOSCRIPTPATH%genWikiFromPPL.bat %MMPATH% %SRCFILE% %PROJNAME%
call %YAIOSCRIPTPATH%genHtmlFromPPL.bat %MMPATH% %SRCFILE% %PROJNAME%
call %YAIOSCRIPTPATH%genMMFromPPL.bat %MMPATH% %SRCFILE% %PROJNAME%
call %YAIOSCRIPTPATH%genExcelFromPPL.bat %MMPATH% %SRCFILE% %PROJNAME%
