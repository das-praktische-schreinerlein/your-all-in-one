rem set pathes
set PLANDIR=%~dp0
set YAIOSCRIPTPATH=%PLANDIR%..\..\..\sbin\
set YAIOBASEPATH=%YAIOSCRIPTPATH%
set BASEPATH=%YAIOBASEPATH%

set PLANFILEBASE=test
set PLANNAME=TestPlanung
set PLANSYSUID=MasterplanMasternode1

:gen-all
rem gen all
copy S:\Daten\gesamtplanung.ppl %PLANDIR%%PLANFILEBASE%.ppl
copy S:\Daten\Projekte\yourallinone\tools\\var\nodeids.properties %YAIOVARPATH%\\nodeids.properties
call %BASEPATH%gen-planung.bat %PLANDIR% %PLANFILEBASE% %PLANNAME% --onlyifstate %PLANSYSUID%
:end

