rem set BASEPATH=D:\Projekte\yaio-devel\yaio\libexec\
set BASEPATH=S:\Daten\Projekte\yourallinone\tools\
set PLANDIR=S:\Daten\Projekte\yourallinone\buero\beispiel\
set PLANNAME=Urlaub
set PLANSYSUID=DT201401231303269651
set SYSUID=%PLANSYSUID%

rem Step1
set PLANFILEBASE=beispiel-step1
rem call %BASEPATH%gen-planung-fromwiki.bat %PLANDIR% %PLANFILEBASE% %PLANNAME%

rem Step2
set PLANFILEBASE=beispiel-step2
rem call %BASEPATH%gen-planung-fromwiki.bat %PLANDIR% %PLANFILEBASE% %PLANNAME%

rem Step3
set PLANFILEBASE=beispiel-step3
rem call %BASEPATH%gen-planung-fromwiki.bat %PLANDIR% %PLANFILEBASE% %PLANNAME%

rem Step4
set PLANFILEBASE=beispiel-step4
rem call %BASEPATH%gen-planung-fromwiki.bat %PLANDIR% %PLANFILEBASE% %PLANNAME%

rem Step5
set PLANFILEBASE=beispiel-step5
rem Datum nach hinten setzen
rem call %BASEPATH%gen-planung-fromwiki.bat %PLANDIR% %PLANFILEBASE% %PLANNAME%
rem Datum nach vorne setzen

rem Step6
set PLANFILEBASE=beispiel-step6
call %BASEPATH%gen-planung-fromwiki.bat %PLANDIR% %PLANFILEBASE% %PLANNAME%
