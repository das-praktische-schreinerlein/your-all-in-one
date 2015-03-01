rem ################
rem # deactivate project-scripts from existing sources
rem # usage: create-projectscripts.bat PROJECTPATH
rem ################

rem parameters
set PROJECTPATH=%1%

rem configure
set PROJECTSCRIPT=%PROJECTPATH%\gen-planung
set VAR=%%

rem your choice
goto deactivategenscript
goto end

:deactivategenscript
rem deactivate projectscript
echo rem ################> %PROJECTSCRIPT%-new.bat
echo rem # warning deprecated>> %PROJECTSCRIPT%-new.bat
echo rem ################>> %PROJECTSCRIPT%-new.bat
echo echo "gen-planung.bat is deprecated. use get-planung-from-app.bat instead">> %PROJECTSCRIPT%-new.bat
echo halt>> %PROJECTSCRIPT%-new.bat
echo goto end>> %PROJECTSCRIPT%-new.bat
echo exit>> %PROJECTSCRIPT%-new.bat
C:\ProgrammePortable\PortableApps\PortableApps\CygwinPortable\App\Cygwin\bin\echo >> %PROJECTSCRIPT%-new.bat
cat %PROJECTSCRIPT%.bat >> %PROJECTSCRIPT%-new.bat
move %PROJECTSCRIPT%-new.bat %PROJECTSCRIPT%.bat

:end
