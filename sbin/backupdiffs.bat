echo on
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     diff 2 files with diff, if there are differences comapre them with windiff and make a backup of the old one
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\backupsdiffs.bat src\test\testproject\test.wiki src\test\testproject\test-real.new.wiki
rem 
rem @package de.yaio
rem @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
rem @category Collaboration
rem @copyright Copyright (c) 2011-2014, Michael Schreiner
rem @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0

rem set CONFIG
set DIFFPATH1=%1%
set DIFFPATH2=%2%

:checkdiff
rem Check 4 diff
set COUNT_DIFF=1

For /F "tokens=1" %%a IN ('diff -rbB --strip-trailing-cr %DIFFPATH1% %DIFFPATH2% ^| wc -l') DO set COUNT_DIFF=%%a
if %COUNT_DIFF% EQU 0 goto nodiff

echo INFO - %COUNT_DIFF% diffences found for %DIFFPATH1%-orig-%TIMESTAMP%.bak %DIFFPATH2%

:makebackup
FOR /f %%a in ('WMIC OS GET LocalDateTime ^| find "."') DO set DTS=%%a
set TIMESTAMP=%DTS:~0,14%
copy %DIFFPATH1% %DIFFPATH1%-orig-%TIMESTAMP%.bak
rem only initial TODO delete if done
copy %DIFFPATH1% %DIFFPATH1%-orig.bak

:overwrite
rem overwrite orig
rem copy %DIFFPATH2% %DIFFPATH1%

:winmerge
rem start winmerge
if "%SHOWINMERGE%" EQU "" goto end
start "" %PROG_WINMERGE% %DIFFPATH1%-orig-%TIMESTAMP%.bak %DIFFPATH2%

goto end

:nodiff
rem go default way
echo INFO - no diffences found for %DIFFPATH1% %DIFFPATH2%
goto end

:end
rem bye
