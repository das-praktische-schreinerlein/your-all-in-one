#!/bin/bash
# create puml and puml.md for class-depends and class-detail-diagramms

# generate diagrams
./gen-puml-for-java.sh "../yaio-app-core/src/main/java/" "../resources/docs/classes/yaio-classes-core" "Core/Domain" "de.yaio.app.core.datadomain|de.yaio.app.core.node" "/de/yaio/app/core/datadomain/,/de/yaio/app/core/node/"
./gen-puml-for-java.sh "../yaio-app-core/src/main/java/" "../resources/docs/classes/yaio-classes-datadomainservice" "Datadomain-Services" "de.yaio.app.core.datadomainservice.*" "/de/yaio/app/core/datadomainservice/"
./gen-puml-for-java.sh "../yaio-app-core/src/main/java/" "../resources/docs/classes/yaio-classes-nodeservice" "Node-Services" "de.yaio.app.core.nodeservice.*" "/de/yaio/app/core/nodeservice/"
./gen-puml-for-java.sh "../yaio-app-core/src/main/java/" "../resources/docs/classes/yaio-classes-datatransfer" "DataTransfer" "de.yaio.app.extension.datatransfer.*" "/de/yaio/app/extension/datatransfer/"
./gen-puml-for-java.sh "../yaio-app-core/src/main/java/" "../resources/docs/classes/yaio-classes-exporter" "Exporter" "de.yaio.app.datatransfer.exporter.*" "/de/yaio/app/datatransfer/exporter/"
./gen-puml-for-java.sh "../yaio-app-core/src/main/java/" "../resources/docs/classes/yaio-classes-importer" "Importer" "de.yaio.app.datatransfer.importer.*" "/de/yaio/app/datatransfer/importer/"
./gen-puml-for-java.sh "../yaio-app-core/src/main/java/" "../resources/docs/classes/yaio-classes-extension-exporter" "Extension Exporter" "de.yaio.app.extension.datatransfer.exporter.*" "/de/yaio/app/extension/datatransfer/exporter/"
./gen-puml-for-java.sh "../yaio-app-core/src/main/java/" "../resources/docs/classes/yaio-classes-extension-importer" "Extension Importer" "de.yaio.app.extension.datatransfer.importer.*" "/de/yaio/app/extension/datatransfer/importer/"
./gen-puml-for-java.sh "../yaio-app-cli/src/main/java/" "../resources/docs/classes/yaio-classes-cli" "Jobs" "de.yaio.app.cli.*" "/de/yaio/app/cli/"
./gen-puml-for-java.sh "../yaio-app-server-standalone/src/main/java/" "../resources/docs/classes/yaio-classes-rest" "RESTfull" "de.yaio.app.server.restcontroller.*" "/de/yaio/app/server/restcontroller/"
./gen-puml-for-java.sh "../yaio-app-server-standalone/src/main/java/" "../resources/docs/classes/yaio-classes-server" "Webapp+Rest" "de.yaio.app.server.*" "/de/yaio/app/server/"
./gen-puml-for-java.sh "../yaio-app-server-standalone/src/main/java/" "../resources/docs/classes/yaio-classes-app" "App/Config" "de.yaio.app.config.*" "/de/yaio/app/config/"
./gen-puml-for-java.sh "../src/main/java/" "../resources/docs/classes/yaio-classes-all" "All" "de.yaio.*" "/de/yaio/"

# concat
cat ../resources/docs/classes/yaio-classes-*_class_details.puml.md > ../resources/docs/classes/yaio-classwa_class_details.puml.md
cat ../resources/docs/classes/yaio-classes-*_class_depends.puml.md > ../resources/docs/classes/yaio-classes_class_dependes.puml.md
cat ../resources/docs/classes/yaio-classes-*_class_all.puml.md > ../resources/docs/classes/yaio-classes_class_all.puml.md