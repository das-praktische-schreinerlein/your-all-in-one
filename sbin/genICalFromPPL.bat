echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     generate the export-media ICal (-events.ics, -tasks.ics) from an existing ppl-file
rem <h4>Syntax:</h4>
rem     PROG projektpath filename_without_extension projectname
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\genICalFromPPL.bat src\test\testproject\ test Testprojekt
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

rem create Event-ICal from PPL
grep EVENT_ %MMPATH%%SRCFILE%.ppl > %MMPATH%%SRCFILE%-events.ppl
set CMD=java %JAVAOPTIONS% -cp %CP% %PROG_ICalN% %CFG% -m %PROJNAME% %SRC_OPTIONS%  %OUTPUT_OPTIONS% %MMPATH%%SRCFILE%-events.ppl
echo "create ICal from PPL: %PROG_ICalN% %CFG% -m %PROJNAME% %SRC_OPTIONS%  %OUTPUT_OPTIONS% %MMPATH%%SRCFILE%-events.ppl > %MMPATH%\%SRCFILE%-events.ics"
%CMD% > %MMPATH%\%SRCFILE%-events.ics

rem create ToDo-ICal from PPL
grep -v EVENT_ %MMPATH%%SRCFILE%.ppl > %MMPATH%%SRCFILE%-tasks.ppl
set CMD=java %JAVAOPTIONS% -cp %CP% %PROG_ICalN% %CFG% -m %PROJNAME% %SRC_OPTIONS%  %OUTPUT_OPTIONS% %MMPATH%%SRCFILE%-tasks.ppl
echo "create ICal from PPL: %PROG_ICalN% %CFG% -m %PROJNAME% %SRC_OPTIONS%  %OUTPUT_OPTIONS% %MMPATH%%SRCFILE%-tasks.ppl > %MMPATH%\%SRCFILE%-tasks.ics"
%CMD% > %MMPATH%\%SRCFILE%-tasks.ics
