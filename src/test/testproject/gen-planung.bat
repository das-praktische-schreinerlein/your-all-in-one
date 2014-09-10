rem set pathes
set PLANDIR=%~dp0
set YAIOSCRIPTPATH=%PLANDIR%..\..\..\sbin\
set YAIOBASEPATH=%YAIOSCRIPTPATH%
set BASEPATH=%YAIOBASEPATH%

set PLANFILEBASE=test
set PLANNAME=TestPlanung
set PLANSYSUID=DT201401231236052431

rem goto regen-from-excel
:gen-all
rem gen all
call %BASEPATH%gen-planung-fromwiki.bat %PLANDIR% %PLANFILEBASE% %PLANNAME% --onlyifstate %PLANSYSUID%
call %BASEPATH%checkdiffs %PLANDIR%%PLANFILEBASE%.wiki %PLANDIR%%PLANFILEBASE%-real.new.wiki

:gen-changelog
rem gen changelog
call %BASEPATH%gen-planungchangelog.bat %PLANDIR% %PLANFILEBASE%-real %PLANNAME%

:gen-lebenslauf
rem gen lebenslauf
set GEN_SRC=ppl
set SRC_OPTIONS=--sourcetype ppl
grep -E "Lebenslauf bis 2013" %PLANDIR%%PLANFILEBASE%-real.ppl > %PLANDIR%%PLANFILEBASE%lebenslauf.ppl
call %BASEPATH%genHtmlFromPPL.bat %PLANDIR% %PLANFILEBASE%lebenslauf %PLANNAME%

:regen-from-excel
rem gen regen from excel
set GEN_SRC=ppl
set SRC_OPTIONS=--sourcetype ppl
cp %PLANDIR%%PLANFILEBASE%-real.xls %PLANDIR%%PLANFILEBASE%-regen.xls
call %BASEPATH%genPPLFromExcel.bat %PLANDIR% %PLANFILEBASE%-regen %PLANNAME%
call %BASEPATH%genWikiFromPPL.bat %PLANDIR% %PLANFILEBASE%-regen %PLANNAME%

:end

