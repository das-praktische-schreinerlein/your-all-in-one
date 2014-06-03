echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     diff 2 files with diff, if there are differences comapre them with windiff
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\checkdiffs.bat src\test\testproject\test.wiki src\test\testproject\test-real.new.wiki
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

For /F "tokens=1" %%a IN ('diff -r --strip-trailing-cr %DIFFPATH1% %DIFFPATH2% ^| wc -l') DO set COUNT_DIFF=%%a
if %COUNT_DIFF% EQU 0 goto nodiff

:winmerge
rem start winmerge
   echo INFO - %COUNT_DIFF% diffences found for %DIFFPATH1% %DIFFPATH2%
   start "" %PROG_WINMERGE% %DIFFPATH1% %DIFFPATH2%
   goto end

:nodiff
rem go default way
echo INFO - no diffences found for %DIFFPATH1% %DIFFPATH2%
goto end

:end
rem bye
