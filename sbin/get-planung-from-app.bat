echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     get the planing-files from running yaioapp
rem <h4>Syntax:</h4>
rem     PROG projektpath filename_without_extension projectname plansysuid
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\get-planung-from-app.bat src\test\testproject\ test Testprojekt DT7565654654654654
rem 
rem @package de.yaio
rem @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
rem @category Collaboration
rem @copyright Copyright (c) 2011-2014, Michael Schreiner
rem @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0

rem set CONFIG
set PLANDIR=%1%
set PLANFILEBASE=%2%
set PLANNAME=%3%
set PLANSYSUID=%4%

rem set pathes
set YAIOSCRIPTPATH=%~dp0
set YAIOBASEPATH=%YAIOSCRIPTPATH%
set BASEPATH=%YAIOBASEPATH%
set YAIOCONFIGPATH=%YAIOSCRIPTPATH%..\config\

rem init config
call %YAIOCONFIGPATH%\config-yaio.bat %YAIOSCRIPTPATH%

rem export wiki
:dowiki
call %BASEPATH%exportFromYAIOApp.bat %PLANDIR% %PLANFILEBASE% %PLANNAME% wiki %PLANSYSUID%

rem generate all other
if NOT "%WIKIONLY%" EQU "" goto end

:otherexports
call %BASEPATH%exportFromYAIOApp.bat %PLANDIR% %PLANFILEBASE% %PLANNAME% html %PLANSYSUID%
mv %PLANDIR%%PLANFILEBASE%.yaioexport.html %PLANDIR%%PLANFILEBASE%.yaioexport-real-export.html

call %BASEPATH%exportFromYAIOApp.bat %PLANDIR% %PLANFILEBASE% %PLANNAME% documentation %PLANSYSUID%
mv %PLANDIR%%PLANFILEBASE%.yaioexport.documentation %PLANDIR%%PLANFILEBASE%.yaioexport-real-docexport.html

call %BASEPATH%exportFromYAIOApp.bat %PLANDIR% %PLANFILEBASE% %PLANNAME% mindmap %PLANSYSUID%
mv %PLANDIR%%PLANFILEBASE%.yaioexport.mindmap %PLANDIR%%PLANFILEBASE%.yaioexport-real.mm

call %BASEPATH%exportFromYAIOApp.bat %PLANDIR% %PLANFILEBASE% %PLANNAME% icalevents %PLANSYSUID%
mv %PLANDIR%%PLANFILEBASE%.yaioexport.icalevents %PLANDIR%%PLANFILEBASE%.yaioexport-real-events.ics

call %BASEPATH%exportFromYAIOApp.bat %PLANDIR% %PLANFILEBASE% %PLANNAME% icaltodos %PLANSYSUID%
mv %PLANDIR%%PLANFILEBASE%.yaioexport.icaltodos %PLANDIR%%PLANFILEBASE%.yaioexport-real-todos.ics

call %BASEPATH%exportFromYAIOApp.bat %PLANDIR% %PLANFILEBASE% %PLANNAME% csv %PLANSYSUID%
mv %PLANDIR%%PLANFILEBASE%.yaioexport.csv %PLANDIR%%PLANFILEBASE%.yaioexport-real.csv

call %BASEPATH%exportFromYAIOApp.bat %PLANDIR% %PLANFILEBASE% %PLANNAME% excel %PLANSYSUID%
mv %PLANDIR%%PLANFILEBASE%.yaioexport.excel %PLANDIR%%PLANFILEBASE%.yaioexport-real.xls

call %BASEPATH%exportFromYAIOApp.bat %PLANDIR% %PLANFILEBASE% %PLANNAME% ppl %PLANSYSUID%
mv %PLANDIR%%PLANFILEBASE%.yaioexport.ppl %PLANDIR%%PLANFILEBASE%.yaioexport-real.ppl

:end
rem Bye