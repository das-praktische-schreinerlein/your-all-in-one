echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     stop the app with rest-services
rem <h4>Syntax:</h4>
rem     PROG
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\stop-yaioapp
rem 
rem @package de.yaio
rem @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
rem @category Collaboration
rem @copyright Copyright (c) 2011-2014, Michael Schreiner
rem @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0

rem set pathes
set ORIGYAIOSCRIPTPATH=%~dp0
set YAIOSCRIPTPATH=%ORIGYAIOSCRIPTPATH%\..\..\sbin\
set YAIOBASEPATH=%YAIOSCRIPTPATH%
set BASEPATH=%YAIOBASEPATH%
set YAIOCONFIGPATH=%YAIOSCRIPTPATH%..\config\

rem init config
cd %YAIOCONFIGPATH%\..
call %YAIOCONFIGPATH%\config-server.bat %YAIOSCRIPTPATH%

echo "stop-yaio-apptest-webdriver: %CMD%"
taskkill /F /FI "WINDOWTITLE eq yaio-apptest-webdriver*"

rem add --debug option to see the startprocess of spring-boot
set CMD=java %JAVAOPTIONS% -cp %CP% %PROG_APP% %CFG% %NEWID_OPTIONS%
echo "stop-yaioapp: %CMD%"
taskkill /F /FI "WINDOWTITLE eq yaio-apptest-app-%YAIOINSTANCE%*"
