set BASEPATH=\Daten\Projekte\yourallinone\tools\
set PLANDIR=\Daten\Urlaube\
set PLANNAME=Urlaube
set PLANFILEBASE=urlaube
set PLANSYSUID=DT201401231303269651
set SYSUID=%PLANSYSUID%

call %BASEPATH%gen-planung-fromwiki.bat %PLANDIR% %PLANFILEBASE% %PLANNAME%
call %BASEPATH%checkdiffs %PLANDIR%%PLANFILEBASE%.wiki %PLANDIR%%PLANFILEBASE%-real.new.wiki
