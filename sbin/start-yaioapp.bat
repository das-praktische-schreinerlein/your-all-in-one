echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     run the app with rest-services
rem <h4>Syntax:</h4>
rem     PROG
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\start-yaioapp
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

rem use correct pathes
set SAVEDPWD=%cd%
set LOCALBASEPATH=%~dp0\..
cd %LOCALBASEPATH%

set CMD=java %JAVAOPTIONS% -cp %CP% %PROG_FLYWAY% %FLYWAYCFG%
echo "run-flyway: %CMD%"
%CMD%

set CMD=java %JAVAOPTIONS% -cp %CP% %PROG_APPPROPAGATOR% %CFG%
echo "start-apppropagator: %CMD%"
start "yaio-apppropagator-%YAIOINSTANCE%" %CMD%

rem add --debug option to see the startprocess of spring-boot
set CMD=java %JAVAOPTIONS% -cp %CP% %PROG_APP% %CFG% %NEWID_OPTIONS%
echo "start-yaioapp: %CMD%"
start "yaio-app-%YAIOINSTANCE%" %CMD%

set CMD=%YAIOSCRIPTPATH%\importMailsFromMailImportQueueToYAIOApp.bat 60
echo "start-yaioapp-mailimportqueue: %CMD%"
start "yaio-app-mailimportqueue-%YAIOINSTANCE%" %CMD%

cd %SAVEDPWD%

:openurl
timeout /T 30 /nobreak
start "" "%STARTURL%"
