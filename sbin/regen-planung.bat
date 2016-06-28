echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     parse the import-Excel-file (.xml) to an ppl-file and generate the 
rem     (MM, Wiki, Html-Exports)
rem <h4>Syntax:</h4>
rem     PROG projektpath filename_without_extension projectname
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\regen-planung.bat src\test\testproject\ test Testprojekt
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
set BASEPATH=%YAIOBASEPATH%
set YAIOCONFIGPATH=%YAIOSCRIPTPATH%..\config\

rem init config
call %YAIOCONFIGPATH%\config-cli.bat %YAIOSCRIPTPATH%

rem set OUTPUT_OPTIONS=-e 2
set OUTPUT_OPTIONS=-e 99 --mergeexcelplanunggantsheets
call %BASEPATH%genPPLFromExcel.bat %MMPATH% %SRCFILE% %PROJNAME%

rem nur Plandaten (kein IST/Status) 
set OUTPUT_OPTIONS=-U 3 -d -e 99 -i -p -intend 80  -intendli 2
rem -s -i -p 
call %BASEPATH%genMMFromPPL.bat %MMPATH% %SRCFILE% %PROJNAME%
call %BASEPATH%genWikiFromPPL.bat %MMPATH% %SRCFILE% %PROJNAME%
call %BASEPATH%genHtmlFromPPL.bat %MMPATH% %SRCFILE% %PROJNAME%
