rem set pathes
set PLANDIR=%~dp0
set YAIOSCRIPTPATH=%PLANDIR%..\..\..\sbin\
set YAIOBASEPATH=%YAIOSCRIPTPATH%
set BASEPATH=%YAIOBASEPATH%

set PLANFILEBASE=test
set PLANNAME=TestPlanung

goto shortway
call %BASEPATH%gen-planung-fromwiki.bat %PLANDIR% %PLANFILEBASE% %PLANNAME% --onlyifstate
call %BASEPATH%checkdiffs %PLANDIR%%PLANFILEBASE%.wiki %PLANDIR%%PLANFILEBASE%-real.new.wiki

call %BASEPATH%genICalFromPPL.bat %PLANDIR% %PLANFILEBASE%-real %PLANNAME%
call %BASEPATH%genCsvFromPPL.bat %PLANDIR% %PLANFILEBASE%-real %PLANNAME%
call %BASEPATH%genMMFromPPL.bat %PLANDIR% %PLANFILEBASE%-real %PLANNAME%
call %BASEPATH%genHtmlFromPPL.bat %PLANDIR% %PLANFILEBASE%-real %PLANNAME%
call %BASEPATH%genJSONFromPPL.bat %PLANDIR% %PLANFILEBASE%-real %PLANNAME%
call %BASEPATH%genExcelFromPPL.bat %PLANDIR% %PLANFILEBASE%-real %PLANNAME%
rem call %BASEPATH%genWikiFromPPL.bat %PLANDIR% %PLANFILEBASE%-real %PLANNAME%

cp %PLANFILEBASE%-real.xls %PLANFILEBASE%-regen.xls
call %BASEPATH%genPPLFromExcel.bat %PLANDIR% %PLANFILEBASE%-regen %PLANNAME%
call %BASEPATH%genWikiFromPPL.bat %PLANDIR% %PLANFILEBASE%-regen %PLANNAME%
goto end

:shortway
call %BASEPATH%gen-planung-fromwiki.bat %PLANDIR% %PLANFILEBASE% %PLANNAME% --onlyifstate
call %BASEPATH%checkdiffs %PLANDIR%%PLANFILEBASE%.wiki %PLANDIR%%PLANFILEBASE%-real.new.wiki

rem bei WikiOnly rest ignoerieren 
if NOT "%WIKIONLY%" EQU "" goto end

rem alles generieren
call %BASEPATH%gen-planungchangelog.bat %PLANDIR% %PLANFILEBASE%-real %PLANNAME%

grep -E "Lebenslauf bis 2013" %PLANDIR%%PLANFILEBASE%-real.ppl > %PLANDIR%%PLANFILEBASE%lebenslauf.ppl
call %BASEPATH%genHtmlFromPPL.bat %PLANDIR% %PLANFILEBASE%lebenslauf %PLANNAME%

:end

