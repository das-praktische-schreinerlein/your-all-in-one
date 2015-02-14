echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     exports wiki from running yaio-app
rem <h4>Syntax:</h4>
rem     PROG projektpath filename_without_extension projectname format sysuid
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\exportFromYAIOApp.bat src\test\testproject\ test Testprojekt wiki 9389175
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
set FORMAT=%4%
set SYSUID=%5%

rem export data from running yaio
set CMD=java %JAVAOPTIONS% -cp %CP% %PROG_CALLYAIOEXPORT% %YAIOAPPURLCONFIG% -sysuid %SYSUID% -format %FORMAT%
rem echo "%CMD% > %MMPATH%\%SRCFILE%.new.%FORMAT%"
%CMD% > %MMPATH%\%SRCFILE%.yaioexport.%FORMAT%
