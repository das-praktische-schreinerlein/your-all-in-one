echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     generate the export-media JSon (.json) from an existing ppl-file
rem <h4>Syntax:</h4>
rem     PROG projektpath filename_without_extension projectname
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\genJSONFromPPL.bat src\test\testproject\ test Testprojekt
rem 
rem @package de.yaio
rem @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
rem @category Collaboration
rem @copyright Copyright (c) 2011-2014, Michael Schreiner
rem @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0

rem set CONFIG
set MMPATH=%1%
set SRCFILE=%2%
set PROJNAME=%3%

rem create JSON from PPL
set CMD=java %JAVAOPTIONS% -cp %CP% %PROG_JSONN% %CFG% -m %PROJNAME% %SRC_OPTIONS%  %OUTPUT_OPTIONS% %MMPATH%%SRCFILE%.ppl
echo "create JSON from PPL: %PROG_JSONN% %CFG% -m %PROJNAME% %SRC_OPTIONS%  %OUTPUT_OPTIONS% %MMPATH%%SRCFILE%.ppl > %MMPATH%\%SRCFILE%.tmp"
rem echo %CMD%
%CMD% > %MMPATH%\%SRCFILE%.tmp
echo { "yourallinoneData": [ > %MMPATH%\%SRCFILE%.json
type %MMPATH%\%SRCFILE%.tmp >> %MMPATH%\%SRCFILE%.json
echo ] } >> %MMPATH%\%SRCFILE%.json
del %MMPATH%\%SRCFILE%.tmp
