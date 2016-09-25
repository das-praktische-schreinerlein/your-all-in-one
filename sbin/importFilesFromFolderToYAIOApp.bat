echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     imports mailfiles from importpath to running yaio-app
rem <h4>Syntax:</h4>
rem     PROG importpath DELETEIFOK
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\importMailsFromFolderToYAIOApp.bat d:\maildir\ DELETEIFOK
rem 
rem @package de.yaio
rem @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
rem @category Collaboration
rem @copyright Copyright (c) 2011-2014, Michael Schreiner
rem @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0

rem set CONFIG
set IMPORTTYPE=%1%
set IMPORTDIRBASEPATH=%2%
set DELETEIFOK=%3%


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

Setlocal EnableDelayedExpansion
for /f "tokens=*" %%A in ('dir /b /s /a:d "%IMPORTDIRBASEPATH%\*"') do (
    set IMPORTDIR=%%A
    set PARENTID=!IMPORTDIR!

    call :methodExtractParentId !PARENTID!

    for %%F in ("!IMPORTDIR!\*") do (
        set IMPORTFILE="%%~nF%%~xF"
        set CMD=call %YAIOSCRIPTPATH%\importFileToYAIOApp.bat %IMPORTTYPE% !IMPORTDIR! !IMPORTFILE! !PARENTID! %DELETEIFOK%
        echo !CMD!
        !CMD!
        IF !ERRORLEVEL! NEQ 0 (
          echo "something went wrong !ERRORLEVEL! for !IMPORTDIR! !IMPORTFILE!"
        ) else (
          echo "everything fine !ERRORLEVEL! for !IMPORTDIR! !IMPORTFILE!"
        )
    )
)

cd %SAVEDPWD%
goto EOF

:methodExtractParentId
    set PARENTID=%1
    set NEWPARENTID=!PARENTID!
    :nextSubString
    for /f "delims=- tokens=1,*" %%B in ("!NEWPARENTID!") do (
        set PARENTID=%%B
        set NEWPARENTID=%%C
    )
    if "!NEWPARENTID!" equ "" goto endSubString
    if "!NEWPARENTID!" equ "!PARENTID!" goto endSubString
    goto nextSubString

    :endSubString
    goto EOF

:EOF

ENDLOCAL