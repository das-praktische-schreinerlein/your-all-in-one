echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     recalc nodes from running yaioapp
rem <h4>Syntax:</h4>
rem     PROG
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\recalcNodesInYAIOApp.bat
rem 
rem @package de.yaio
rem @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
rem @category Collaboration
rem @copyright Copyright (c) 2011-2014, Michael Schreiner
rem @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0

rem init config
set NODEID=%1%

rem set pathes
set YAIOSCRIPTPATH=%~dp0
set YAIOBASEPATH=%YAIOSCRIPTPATH%
set BASEPATH=%YAIOBASEPATH%
set YAIOCONFIGPATH=%YAIOSCRIPTPATH%..\config\


call %YAIOCONFIGPATH%\config-yaio.bat %YAIOSCRIPTPATH%
set CMD=java %JAVAOPTIONS% -cp %CP% %PROG_CALLYAIORECALC% %YAIOAPPURLCONFIG% -sysuid %NODEID%
echo "callYaioRecalcNodes: %CMD%"
%CMD%
