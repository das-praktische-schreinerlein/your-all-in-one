echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     imports mailfile to running yaio-app
rem <h4>Syntax:</h4>
rem     PROG projektpath filename_with_extension parentsysuid
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\importMailToYAIOApp.bat src\test\testproject\ test.eml  MasterNode1
rem 
rem @package de.yaio
rem @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
rem @category Collaboration
rem @copyright Copyright (c) 2011-2014, Michael Schreiner
rem @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0

rem set CONFIG
set MMPATH=%1%
set SRCFILE=%~2
set SYSUID=%3%
set DELETEIFOK=%4%

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

set MYRETCODE=0

rem import data to running yaio
set CMD=java %JAVAOPTIONS% -cp %CP% %PROG_CALLYAIOIMPORT% %YAIOAPPURLCONFIG% -parentsysuid %SYSUID% -importtype mail -importfile "%MMPATH%\%SRCFILE%"
rem echo "%CMD%"
%CMD%
IF %ERRORLEVEL% NEQ 0 (
  echo "mail import failed: %MMPATH%\%SRCFILE%"
  set MYRETCODE=1
  goto END
)

echo "mail import done: %MMPATH%\%SRCFILE%"

if "%DELETEIFOK%" NEQ "" (
  echo "delete: %MMPATH%\%SRCFILE%"
  del "%MMPATH%\%SRCFILE%"
)

:END
cd %SAVEDPWD%
exit /B %MYRETCODE%
