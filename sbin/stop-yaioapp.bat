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
set YAIOSCRIPTPATH=%~dp0
set YAIOBASEPATH=%YAIOSCRIPTPATH%
set BASEPATH=%YAIOBASEPATH%
set YAIOCONFIGPATH=%YAIOSCRIPTPATH%..\config\

rem init config
call %YAIOCONFIGPATH%\config-server.bat %YAIOSCRIPTPATH%

set CMD=java %JAVAOPTIONS% -cp %APPPROPAGATOR% %PROG_APPPROPAGATOR% %CFG%
echo "stop-apppropagator: %CMD%"
taskkill /F /FI "WINDOWTITLE eq yaio-apppropagator-%YAIOINSTANCE%*"

set CMD=java %JAVAOPTIONS% -cp %CP% %PROG_APP% %CFG% %NEWID_OPTIONS%
echo "stop-yaioapp: %CMD%"
rem bullshit springapp cant regulary stop :-( taskkill /T /FI "WINDOWTITLE eq yaio-app-%YAIOINSTANCE%*"

set CMD=%YAIOSCRIPTPATH%\importMailsFromMailImportQueueToYAIOApp.bat 60
echo "stop-yaioapp-mailimportqueue: %CMD%"
taskkill /F /FI "WINDOWTITLE eq yaio-app-mailimportqueue-%YAIOINSTANCE%*"

set CMD=%YAIOSCRIPTPATH%\importFilesFromFileImportQueueToYAIOApp.bat 60
echo "stop-yaioapp-fileimportqueue: %CMD%"
taskkill /F /FI "WINDOWTITLE eq yaio-app-fileimportqueue-%YAIOINSTANCE%*"
