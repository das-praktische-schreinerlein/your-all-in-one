echo off
#  <h4>FeatureDomain:</h4>
#      Collaboration
#  <h4>FeatureDescription:</h4>
#      exports wiki from running yaio-app
#  <h4>Syntax:</h4>
#      PROG projektpath filename_without_extension projectname format sysuid
#  <h4>Example:</h4>
#      cd D:\public_projects\yaio\yaio
#      sbin\exportFromYAIOApp.bat src\test\testproject\ test Testprojekt wiki 9389175
#  
#  @package de.yaio
#  @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
#  @category Collaboration
#  @copyright Copyright (c) 2011-2014, Michael Schreiner
#  @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0

#  set CONFIG
MMPATH=${1}
SRCFILE=${2}
PROJNAME=${3}
FORMAT=${4}
SYSUID=${5}

#  export data from running yaio
CMD=java ${JAVAOPTIONS} -cp ${CP} ${PROG_CALLYAIOEXPORT} ${YAIOAPPURLCONFIG} -sysuid ${SYSUID} -format ${FORMAT} -outfile ${MMPATH}\${SRCFILE}.yaioexport.${FORMAT}
#  echo "${CMD} > ${MMPATH}\${SRCFILE}.new.${FORMAT}"
# ${CMD} > ${MMPATH}\${SRCFILE}.yaioexport.${FORMAT}
${CMD}
