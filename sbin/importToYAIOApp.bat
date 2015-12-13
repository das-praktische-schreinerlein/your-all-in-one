echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     imports wiki to running yaio-app
rem <h4>Syntax:</h4>
rem     PROG projektpath filename_with_extension parentsysuid
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\importToYAIOApp.bat src\test\testproject\ test.wiki  MasterNode1
rem 
rem @package de.yaio
rem @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
rem @category Collaboration
rem @copyright Copyright (c) 2011-2014, Michael Schreiner
rem @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0

rem set CONFIG
set MMPATH=%1%
set SRCFILE=%2%
set SYSUID=%3%

rem import data to running yaio
set CMD=java %JAVAOPTIONS% -cp %CP% %PROG_CALLYAIOIMPORT% %YAIOAPPURLCONFIG% -parentsysuid %SYSUID% -importfile %MMPATH%\%SRCFILE%
rem echo "%CMD%"
%CMD%
