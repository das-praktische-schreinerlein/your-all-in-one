#!/bin/bash
# create puml and puml.md for class-depends and class-detail-diagramms

# generate diagrams
./gen-puml-for-java.sh "../src/main/java/" "../resources/docs/classes/yaio-classes-core" "Core/Domain" "de.yaio.core.datadomain|de.yaio.core.node" "/de/yaio/core/datadomain/,/de/yaio/core/node/" 
./gen-puml-for-java.sh "../src/main/java/" "../resources/docs/classes/yaio-classes-datadomainservice" "Datadomain-Services" "de.yaio.core.datadomainservice.*" "/de/yaio/core/datadomainservice/"
./gen-puml-for-java.sh "../src/main/java/" "../resources/docs/classes/yaio-classes-nodeservice" "Node-Services" "de.yaio.core.nodeservice.*" "/de/yaio/core/nodeservice/"
./gen-puml-for-java.sh "../src/main/java/" "../resources/docs/classes/yaio-classes-datatransfer" "DataTransfer" "de.yaio.extension.datatransfer.*" "/de/yaio/extension/datatransfer/"
./gen-puml-for-java.sh "../src/main/java/" "../resources/docs/classes/yaio-classes-exporter" "Exporter" "de.yaio.datatransfer.exporter.*" "/de/yaio/datatransfer/exporter/"
./gen-puml-for-java.sh "../src/main/java/" "../resources/docs/classes/yaio-classes-importer" "Importer" "de.yaio.datatransfer.importer.*" "/de/yaio/datatransfer/importer/"
./gen-puml-for-java.sh "../src/main/java/" "../resources/docs/classes/yaio-classes-extension-exporter" "Extension Exporter" "de.yaio.extension.datatransfer.exporter.*" "/de/yaio/extension/datatransfer/exporter/"
./gen-puml-for-java.sh "../src/main/java/" "../resources/docs/classes/yaio-classes-extension-importer" "Extension Importer" "de.yaio.extension.datatransfer.importer.*" "/de/yaio/extension/datatransfer/importer/"
./gen-puml-for-java.sh "../src/main/java/" "../resources/docs/classes/yaio-classes-jobs" "Jobs" "de.yaio.jobs.*" "/de/yaio/jobs/"
./gen-puml-for-java.sh "../src/main/java/" "../resources/docs/classes/yaio-classes-rest" "RESTfull" "de.yaio.webapp.restcontroller.*" "/de/yaio/webapp/restcontroller/"
./gen-puml-for-java.sh "../src/main/java/" "../resources/docs/classes/yaio-classes-webapp" "Webapp+Rest" "de.yaio.webapp.*" "/de/yaio/webapp/"
./gen-puml-for-java.sh "../src/main/java/" "../resources/docs/classes/yaio-classes-app" "App/Config" "de.yaio.app.*" "/de/yaio/app/"
./gen-puml-for-java.sh "../src/main/java/" "../resources/docs/classes/yaio-classes-all" "All" "de.yaio.*" "/de/yaio/"

# concat
cat ../resources/docs/classes/yaio-classes-*_class_details.puml.md > ../resources/docs/classes/yaio-classwa_class_details.puml.md
cat ../resources/docs/classes/yaio-classes-*_class_depends.puml.md > ../resources/docs/classes/yaio-classes_class_dependes.puml.md
cat ../resources/docs/classes/yaio-classes-*_class_all.puml.md > ../resources/docs/classes/yaio-classes_class_all.puml.md