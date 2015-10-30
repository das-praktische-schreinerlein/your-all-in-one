#!/bin/bash

PLANTUMLDEPCLI=D:\\ProgrammeShared\\plantuml-dependency-cli-1.4.0\\plantuml-dependency-cli-1.4.0-jar-with-dependencies.jar
PLANTUMLDOCLET="D:\\ProgrammeShared\\plantUmlDoclet.jar"
JAVADOCBIN=/cygdrive/d/Programme/Java/javadoc1.7.exec
FINDBIN=/cygdrive/c//ProgrammePortable/PortableApps/PortableApps/CygwinPortable/App/Cygwin/bin/find

# usage:
#     replace_text_in_file SRCFILE SRC INSERTFILE
replace_text_in_file() 
{
    REPLACEMENT=$(echo $3 | sed -e 's/[]\/$*.^|[]/\\&/g')
    sed s/$2/$REPLACEMENT/g ${1} > ${1}.tmp_replace_text_in_file_$$
    mv ${1}.tmp_replace_text_in_file_$$ ${1}
}
# usage:
#     replace_text_with_file SRCFILE SRC INSERTFILE
replace_text_with_file() 
{
    pattern=
    sed "/$2/r $3" ${1} > ${1}.tmp_replace_text_with_file_$$
    sed s/$2//g ${1}.tmp_replace_text_with_file_$$ > ${1}
    rm ${1}.tmp_replace_text_with_file_$$
}

# create puml-file /from template-class-depends.puml) for class-depends
# usage:
#     yaio_create_puml_for_class_depends SRCDIR PUMLFILEBASE LABEL PACKAGEFILTER
#        SRCDIR        Source-dir  "../src/main/java/"
#        PUMLFILEBASE  filebase without ending "yaio-classes"
#        LABEL         label to use in  diagram and markdown "Yaio"
#        PACKAGEFILTER package-filter for class_depends-diagrams "de.yaio.*"
yaio_create_puml_for_class_depends()
{
    SRCDIR=$1
    PUMLFILE=$2
    LABEL=$3
    PACKAGEFILTER=$4

    # exec command
    COMMAND="java -jar ${PLANTUMLDEPCLI}  -o ${PUMLFILE}.tmp_$$ -b ${SRCDIR} -i **/*.java -e **/*Test*.java --display-package-name ${PACKAGEFILTER} --display-type abstract_classes,annotations,classes,enums,extensions,implementations,imports,interfaces,native_methods,static_imports"
    ${COMMAND}
    
    # cut heading @startuml @enduml from file
    head -n "-1" $PUMLFILE.tmp_$$ | tail -n +2 > $PUMLFILE.tmp2_$$

    # create md-file
    cp template-class-depends.puml ${PUMLFILE}

    # replace placeholder in files
    replace_text_with_file ${PUMLFILE} XXXPUMLXXX $PUMLFILE.tmp2_$$
    replace_text_in_file ${PUMLFILE} XXXPUMLLABELXXX $LABEL
    cat -s ${PUMLFILE} > ${PUMLFILE}.tmp_$$
    mv ${PUMLFILE}.tmp_$$ ${PUMLFILE}
    rm ${PUMLFILE}.tmp2_$$
}

# create puml-file (from template-class-details.puml) for class-detail-diagrams
# usage:
#     yaio_create_puml_for_class_details SRCDIR PUMLFILEBASE LABEL DIRFILTER
#        SRCDIR        Source-dir  "../src/main/java/"
#        PUMLFILE      filebase witht ending "yaio-classes.puml"
#        LABEL         label to use in  diagram and markdown "Yaio"
#        DIRFILTER     dirs for class-detail-diagrams "/de/yaio/"
yaio_create_puml_for_class_details()
{
    SRCDIR=$1
    PUMLFILE=$2
    LABEL=$3
    DIRFILTER=$4

    # read files
    IFS=',' read -ra ADDR <<< "$DIRFILTER"
    FULLDIRFILTER=
    for i in "${ADDR[@]}"; do
        FULLDIRFILTER="$FULLDIRFILTER ${SRCDIR}/$i"
    done
    FILES=$($FINDBIN $FULLDIRFILTER -iname *.java)
    
    # exec command
    COMMAND="$JAVADOCBIN -docletpath ${PLANTUMLDOCLET} -J-DdestinationFile=${PUMLFILE}.tmp_$$ -J-DcreatePackages=false -J-DshowPublicMethods=true -J-DshowPublicConstructors=false -doclet de.mallox.doclet.PlantUMLDoclet ${FILES}"
    #COMMAND="/cygdrive/d/Programme/Java/jdk1.8.0_60/bin/java.exe -cp ${PLANTUMLDOCLET} -DdestinationFile=${PUMLFILE}.tmp_$$ -DcreatePackages=false -DshowPublicMethods=true -DshowPublicConstructors=false de.mallox.doclet.Runner ${SRCDIR}"
    ${COMMAND}
    #echo ${COMMAND}
    
    # cut heading @startuml @enduml from file
    head -n "-1" $PUMLFILE.tmp_$$ | tail -n +2 > $PUMLFILE.tmp2_$$

    # create md-file
    cp template-class-details.puml ${PUMLFILE}

    # replace placeholder in files
    replace_text_with_file ${PUMLFILE} XXXPUMLXXX $PUMLFILE.tmp2_$$
    replace_text_in_file ${PUMLFILE} XXXPUMLLABELXXX $LABEL
    cat -s ${PUMLFILE} > ${PUMLFILE}.tmp_$$
    mv ${PUMLFILE}.tmp_$$ ${PUMLFILE}
    rm ${PUMLFILE}.tmp2_$$
}

# create puml-file (from template-class-depends.puml) and puml.md-file (from template-class-depends.md) for class-depends
# usage:
#     yaio_create_pumlmd_for_class_depends SRCDIR PUMLFILEBASE LABEL PACKAGEFILTER
#        SRCDIR        Source-dir  "../src/main/java/"
#        PUMLFILEBASE  filebase without ending "yaio-classes"
#        LABEL         label to use in  diagram and markdown "Yaio"
#        PACKAGEFILTER package-filter for class_depends-diagrams "de.yaio.*"
yaio_create_pumlmd_for_class_depends()
{
    SRCDIR=$1
    PUMLFILE=$2
    LABEL=$3
    PACKAGEFILTER=$4

    yaio_create_puml_for_class_depends $SRCDIR $PUMLFILE $LABEL $PACKAGEFILTER

    # create md-file
    cp template-class-depends.md ${PUMLFILE}.md

    # gen GENERATOR
    GENERATOR=$(echo $COMMAND) 

    # replace placeholder in files
    replace_text_with_file ${PUMLFILE}.md XXXPUMLXXX $PUMLFILE
    replace_text_in_file ${PUMLFILE}.md XXXPUMLLABELXXX $LABEL
    replace_text_in_file ${PUMLFILE}.md XXXPUMLGENERATORXXX ${GENERATOR}
}

# create puml-file (from template-class.puml) and puml.md-file (from template-class-details.md) for class-detail-diagrams
# usage:-details
#     yaio_create_pumlmd_for_class_details SRCDIR PUMLFILEBASE LABEL DIRFILTER
#        SRCDIR        Source-dir  "../src/main/java/"
#        PUMLFILE      filebase witht ending "yaio-classes.puml"
#        LABEL         label to use in  diagram and markdown "Yaio"
#        DIRFILTER     dirs for class-detail-diagrams "/de/yaio/"
yaio_create_pumlmd_for_class_details()
{
    SRCDIR=$1
    PUMLFILE=$2
    LABEL=$3
    DIRFILTER=$4

    yaio_create_puml_for_class_details $SRCDIR $PUMLFILE $LABEL $DIRFILTER

    # create md-file
    cp template-class-details.md ${PUMLFILE}.md

    # gen GENERATOR
    GENERATOR=$(echo $COMMAND) 

    # replace placeholder in files
    replace_text_with_file ${PUMLFILE}.md XXXPUMLXXX $PUMLFILE
    replace_text_in_file ${PUMLFILE}.md XXXPUMLLABELXXX $LABEL
    replace_text_in_file ${PUMLFILE}.md XXXPUMLGENERATORXXX ${GENERATOR}
}
