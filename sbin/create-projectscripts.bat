rem ################
rem # create project-scripts from existing sources
rem # usage: create-projectscripts.bat PROJECTPATH
rem ################

rem parameters
set PROJECTPATH=%1%

rem configure
set PROJECTSCRIPT=%PROJECTPATH%\gen-planung
set CONFIGSCRIPT=%PROJECTPATH%\configure-project.bat
set EXPORTSCRIPT=%PROJECTPATH%\get-planung-from-app.bat
set VAR=%%

rem your choice
goto createexportscript
goto creategenscript
goto createconfig
goto end

:createconfig
rem create config-script
echo rem ################ > %CONFIGSCRIPT%
echo rem # configure project >> %CONFIGSCRIPT%
echo rem ################ >> %CONFIGSCRIPT%
grep "set PLANDIR=" %PROJECTSCRIPT%.bat >> %CONFIGSCRIPT%
grep "set PLANFILEBASE=" %PROJECTSCRIPT%.bat >> %CONFIGSCRIPT%
grep "set PLANNAME=" %PROJECTSCRIPT%.bat >> %CONFIGSCRIPT%
grep "set PLANSYSUID=" %PROJECTSCRIPT%.bat >> %CONFIGSCRIPT%
echo set SYSUID=%VAR%PLANSYSUID%VAR%>> %CONFIGSCRIPT%

:creategenscript
rem create new projectscript
copy %PROJECTSCRIPT%.bat %PROJECTSCRIPT%-old.bat
echo rem ################> %PROJECTSCRIPT%-new.bat
echo rem # generate project>> %PROJECTSCRIPT%-new.bat
echo rem ################>> %PROJECTSCRIPT%-new.bat
C:\ProgrammePortable\PortableApps\PortableApps\CygwinPortable\App\Cygwin\bin\echo >> %PROJECTSCRIPT%-new.bat
echo rem configure>> %PROJECTSCRIPT%-new.bat
echo set PLANDIR=%VAR%~dp0>> %PROJECTSCRIPT%-new.bat
echo call %VAR%PLANDIR%VAR%configure-project.bat>> %PROJECTSCRIPT%-new.bat
C:\ProgrammePortable\PortableApps\PortableApps\CygwinPortable\App\Cygwin\bin\echo >> %PROJECTSCRIPT%-new.bat
echo rem rock'n'roll>> %PROJECTSCRIPT%-new.bat
cat %PROJECTSCRIPT%.bat | grep -v "set PLANDIR=" | grep -v "set PLANFILEBASE=" | grep -v "set PLANNAME=" | grep -v "set PLANSYSUID=" | grep -v "set SYSUID=" >> %PROJECTSCRIPT%-new.bat
move %PROJECTSCRIPT%-new.bat %PROJECTSCRIPT%.bat

:createexportscript
rem create new projectscript
echo rem ################> %EXPORTSCRIPT%
echo rem # export project>> %EXPORTSCRIPT%
echo rem ################>> %EXPORTSCRIPT%
C:\ProgrammePortable\PortableApps\PortableApps\CygwinPortable\App\Cygwin\bin\echo >> %EXPORTSCRIPT%
echo rem configure>> %EXPORTSCRIPT%
echo set PLANDIR=%VAR%~dp0>> %EXPORTSCRIPT%
echo call %VAR%PLANDIR%VAR%configure-project.bat>> %EXPORTSCRIPT%
C:\ProgrammePortable\PortableApps\PortableApps\CygwinPortable\App\Cygwin\bin\echo >> %EXPORTSCRIPT%
echo rem rock'n'roll>> %EXPORTSCRIPT%
echo set BASEPATH=\Daten\Projekte\yourallinone\yaio-prod\sbin\>> %EXPORTSCRIPT%
echo call %VAR%BASEPATH%VAR%get-planung-from-app.bat %VAR%PLANDIR%VAR% %VAR%PLANFILEBASE%VAR% %VAR%PLANNAME%VAR% %VAR%PLANSYSUID%VAR%>> %EXPORTSCRIPT%
echo call %VAR%BASEPATH%VAR%backupdiffs %VAR%PLANDIR%VAR%%VAR%PLANFILEBASE%VAR%.wiki %VAR%PLANDIR%VAR%%VAR%PLANFILEBASE%VAR%.yaioexport.wiki>> %EXPORTSCRIPT%
echo goto end>> %EXPORTSCRIPT%
C:\ProgrammePortable\PortableApps\PortableApps\CygwinPortable\App\Cygwin\bin\echo >> %EXPORTSCRIPT%
echo :end>> %EXPORTSCRIPT%

:end