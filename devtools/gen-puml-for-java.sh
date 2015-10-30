#!/bin/bash
# create puml and puml.md for class-depends and class-detail-diagrams
# usage:
#     gen-puml-for-java.sh SRCDIR PUMLFILEBASE LABEL PACKAGEFILTER DIRFILTER
#        SRCDIR        Source-dir  "../src/main/java/"
#        PUMLFILEBASE  filebase without ending "yaio-classes"
#        LABEL         label to use in  diagram and markdown "Yaio"
#        PACKAGEFILTER package-filter for class_depends-diagrams "de.yaio.*"
#        DIRFILTER     dirs for class-detail-diagrams "/de/yaio/"

# set params
SRCDIR=$1
PUMLFILEBASE=$2
LABEL=$3
PACKAGEFILTER=$4
DIRFILTER=$5

# load utils
source utils.bash

# create class_depends
TMPLABEL=$LABEL
yaio_create_pumlmd_for_class_depends $SRCDIR ${PUMLFILEBASE}_class_depends.puml $TMPLABEL $PACKAGEFILTER
# create class-details
TMPLABEL=$LABEL
yaio_create_pumlmd_for_class_details $SRCDIR ${PUMLFILEBASE}_class_details.puml $TMPLABEL $DIRFILTER

# concat both
cat ${PUMLFILEBASE}_class_depends.puml.md ${PUMLFILEBASE}_class_details.puml.md > ${PUMLFILEBASE}_class_all.puml.md
