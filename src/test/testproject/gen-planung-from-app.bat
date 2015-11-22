rem set pathes
set PLANDIR=%~dp0
set YAIOSCRIPTPATH=%PLANDIR%..\..\..\sbin\
set YAIOBASEPATH=%YAIOSCRIPTPATH%
set BASEPATH=%YAIOBASEPATH%

set PLANFILEBASE=test
set PLANNAME=TestPlanung
set PLANSYSUID=MasterplanMasternode1

call %BASEPATH%get-planung-from-app.bat %PLANDIR% %PLANFILEBASE% %PLANNAME% %PLANSYSUID%

:end

