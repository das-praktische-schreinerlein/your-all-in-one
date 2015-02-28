echo off
#  <h4>FeatureDomain:</h4>
#      Collaboration
#  <h4>FeatureDescription:</h4>
#      imports wiki to running yaio-app
#  <h4>Syntax:</h4>
#      PROG projektpath filename_with_extension parentsysuid
#  <h4>Example:</h4>
#      cd D:\public_projects\yaio\yaio
#      sbin\importToYAIOApp.bat src\test\testproject\ test.wiki  MasterNode1
#  
#  @package de.yaio
#  @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
#  @category Collaboration
#  @copyright Copyright (c) 2011-2014, Michael Schreiner
#  @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0

#  set CONFIG
MMPATH=${1}
SRCFILE=${2}
SYSUID=${3}

#  export data from running yaio
CMD=java ${JAVAOPTIONS} -cp ${CP} ${PROG_CALLYAIOIMPORT} ${YAIOAPPURLCONFIG} -parentsysuid ${SYSUID} -importfile ${MMPATH}\${SRCFILE}
#  echo "${CMD}"
${CMD}
