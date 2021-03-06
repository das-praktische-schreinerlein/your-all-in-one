echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     run the app with rest-services and a webdriver-manager for e2e-tests
rem <h4>Syntax:</h4>
rem     PROG
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\start-yaioapp-test
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

set YAIOAPP=%BASEPATH%..\yaio-app-e2e\target\yaio.jar
set CP="%YAIOAPP%;%APPPROPAGATOR%;"

rem init webdriver
set CMD=%ORIGYAIOSCRIPTPATH%../node_modules/.bin/webdriver-manager update --ie
echo "update webdriver %CMD%"
call %CMD%

rem start webdriver
cd %ORIGYAIOSCRIPTPATH%\..
set CMD=%ORIGYAIOSCRIPTPATH%../node_modules/.bin/webdriver-manager start
echo "start webdriver %CMD%"
start "yaio-apptest-webdriver" %CMD%

cd %YAIOSCRIPTPATH%\..
set CMD=java %JAVAOPTIONS% -cp %CP% %PROG_FLYWAY% %FLYWAYCFG%
echo "run-flyway: %CMD%"
%CMD%

rem add --debug option to see the startprocess of spring-boot
set CMD=java %JAVAOPTIONS% -cp %CP% %PROG_APP% %CFG% %NEWID_OPTIONS%
echo "start-yaioapp: %CMD%"
start "yaio-apptest-app-%YAIOINSTANCE%" %CMD%
