echo off
rem <h4>FeatureDomain:</h4>
rem     Collaboration
rem <h4>FeatureDescription:</h4>
rem     parse and generate all from an existing wiki-file (basefile -> basefile-real)
rem <h4>Syntax:</h4>
rem     PROG projektpath filename_without_extension projectname
rem <h4>Example:</h4>
rem     cd D:\public_projects\yaio\yaio
rem     sbin\gen-planung-fromwiki.bat src\test\testproject\ test Testprojekt
rem 
rem @package de.yaio
rem @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
rem @category Collaboration
rem @copyright Copyright (c) 2011-2014, Michael Schreiner
rem @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0

rem set CONFIG
set MYPLANDIR=%1%
set MYPLANFILEBASE=%2%
set MYPLANNAME=%3%
set OPTION=%4%
set SYSUID=%5%

rem set pathes
set YAIOSCRIPTPATH=%~dp0
set YAIOBASEPATH=%YAIOSCRIPTPATH%
set BASEPATH=%YAIOBASEPATH%

copy %MYPLANDIR%%MYPLANFILEBASE%.wiki %MYPLANDIR%%MYPLANFILEBASE%-real.wiki

call %YAIOSCRIPTPATH%gen-planung.bat %MYPLANDIR% %MYPLANFILEBASE%-real %MYPLANNAME% %OPTION% %SYSUID%
del %MYPLANDIR%%MYPLANFILEBASE%-real.wiki
