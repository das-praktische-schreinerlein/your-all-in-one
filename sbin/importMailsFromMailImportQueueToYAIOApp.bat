echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     imports mailfiles from mailimportqueue to running yaio-app
rem <h4>Syntax:</h4>
rem     PROG REPEATEVERYSECONDS
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\importMailsFromMailImportQueueToYAIOApp.bat
rem 
rem @package de.yaio
rem @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
rem @category Collaboration
rem @copyright Copyright (c) 2011-2014, Michael Schreiner
rem @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0

set REPEATEVERYSECONDS=%1

rem set pathes
set YAIOSCRIPTPATH=%~dp0
set YAIOBASEPATH=%YAIOSCRIPTPATH%
set BASEPATH=%YAIOBASEPATH%
set YAIOCONFIGPATH=%YAIOSCRIPTPATH%..\config\

rem init config
call %YAIOCONFIGPATH%\config-client.bat %YAIOSCRIPTPATH%

rem use correct pathes
set SAVEDPWD=%cd%
set LOCALBASEPATH=%~dp0\..
cd %LOCALBASEPATH%

:startloop
set CMD=call %YAIOSCRIPTPATH%\importMailsFromFolderToYAIOApp.bat %YAIOSCRIPTPATH%\..\queues\mailimportqueue\ DELETE
echo %CMD%
%CMD%

if "%REPEATEVERYSECONDS%" EQU "" goto endloop

if %REPEATEVERYSECONDS% GTR 0 (
    timeout /T %REPEATEVERYSECONDS% /nobreak
    goto startloop
  )
)

:endloop
cd %SAVEDPWD%
