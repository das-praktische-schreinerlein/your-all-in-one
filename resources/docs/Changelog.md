# Changelog

## Changelog for release: feature-YAIO2168-improvements-201509-RELEASE
- **Admin/Doku-Funktionen (YAIO2228)**
    - added swagger for api-documentation (YAIO2229)
    - added scripts to recalc data per project (YAIO2230)
- **neue Funktionen (YAIO2174)**
    - added json-export from server (YAIO2175)
    - improved searchfunctions (YAIO2220)
    - added fat multiselect-boxes for search (YAIO2221)
    - added nodefilter on show-view (YAIO2222)
    - added dashboard (YAIO2223)
    - added new function: copy/MoveNode per GUI (YAIO2224)
    - added new function: edit to contextmenu improved nodeCommand-handling (contextmenu) (YAIO2225)
    - added new function: create nodes by template (YAIO2226)
    - added plantUML as new diagram-type (YAIO2231)
    - Taskanlegen über Fixture-Import (YAIO2159)
    - job to recalc nodes (YAIO2227)
    - Server (YAIO2184)
        - added export as yaioOfflineApp (YAIO2185)
        - added json-import to server (YAIO2186)
    - App (YAIO2215)
        - added StaticNodeDataService to use YaioApp without Server (YAIO2176)
            - added json-export of static data (YAIO2177)
            - added save, create, remove for static-datasource (YAIO2178)
            - added move for static datasources (YAIO2179)
            - added search for static datasource (YAIO2180)
            - introduced staticdatastore for better structure (YAIO2181)
        - added FileLoader for YaioJson (YAIO2183)
        - add datasourceselect per parameter (YAIO2216)
        - added sourceselector to switch between server, static, file (YAIO2182)
        - added FileService to load static files (YAIO2217)
        - added module to load/render static content (helpsite...) in offline version (YAIO2218)
        - added static help-content (YAIO2219)
- **Code-Verbesserungen (YAIO2187)**
    - codeclean (YAIO2188)
    - use relative resBaseUrl (YAIO2211)
    - fork vendors with yaio-patches on github and use them (YAIO2189)
    - improved package structure: new service datatransferUtils (YAIO2212)
    - Verbesserungen YaioApp (YAIO2190)
        - modular contentloading (YAIO2191)
        - modularize templates (YAIO2192)
        - configure services by name and use aliases for usage (YAIO2193)
        - introduce NodeDataService-interface and implementation (YAIO2194)
        - added accessmanager to configure urls and check permissions (YAIO2195)
    - improved naming for unknown task/event-state: not planed (YAIO2213)
- **Bugfixes (YAIO2196)**
    - bugfix: hide speechsynth if not availiable (YAIO2197)
    - bugfix: dont use const in js (YAIO2205)
    - fixed tests for iexplorer, firefox (YAIO2198)
    - bugfix: open all exports with target _blank (YAIO2206)
    - fixed logout (YAIO2199)
    - fixed migration-scripts (YAIO2207)

## Changelog for release: feature-YAIO2137-improvements-201507-RELEASE
- **GUI (YAIO2143)**
    - Link für Infonode mit Snapshot des Checkliste/Gantt-Overview für der Node (YAIO2161)
- **Code-Verbesserungen (YAIO2144)**
    - Erweiterung der e2e-Tests (YAIO2162)
    - Fix der e2e-Tests für phantomjs (YAIO2163)
    - Refactoring der ExplorerApp (YAIO2164)
    - Modularisierung der ExplorerApp (YAIO2165)
    - Verbesserung der Paket-Struktur (YAIO2166)
    - Semantic-Versionierung der Js/App+Assets einführen (YAIO2167)

## Changelog for release: feature-YAIO2118-improvements-201505-RELEASE
- **GUI(YAIO2124)**
    - Link für Auswahl des Ganttbereichs Plan/Ist für Masternode(YAIO2133)
- **Code-Verbesserungen(YAIO2125)**
    - Node-Abhängigkeiten über npm auflösen (YAIO2153)
    - Analyse und Update der JS-Bibliotheken und Versionen (YAIO2158)
    - Wechsel von qunit zu jasmin (YAIO2160)
    - Einführung von protractor für e2e-Tests (YAIO2154)
    - Einführung von grunt als Taskrunner (YAIO2155)
    - Implementierung der Selenium-Tests als Protractor e2e-tests (YAIO2156)
    - Concat/Minify der JS/CSS-Ressourcen (YAIO2157)

## Changelog for release: feature-YAIO2082-improvements-201504-RELEASE
- **Neue Funktionen (YAIO2094)**
    - Textsnippets in Suche anzeigen (YAIO2104)
    - added Markdown-Checklists (YAIO2109)
    - YAIO-Refs im Markdown (YAIO2110)
- **GUI (YAIO2091)**
    - Styles fixen (YAIO2103)
- **Code-Verbesserungen (YAIO2097)**
    - Startscript für Linux (YAIO2105)
    - Fixing Translation + State (YAIO2111)
    - Bugfixung Maven-Config (versions, testresources) (YAIO2112)
    - Integration JS-Testing in Deploy-Prozess (YAIO2099)
    - Refactoring Java-Code (Services, Util-Classes, Factories) (YAIO2113)
    - configure MaxFileSize for Upload (YAIO2114)
- **Erweiterungen (YAIO2049)**
    - manuelle Install Vagrant-Ubuntu-Devbox (YAIO2115)
    - Apppropagartor (YAIO2116)
    - YAIO-Demo-Installer (YAIO2117)

## Changelog for release: feature-YAIO2040-improvements-201503-RELEASE
- **Neue Funktionen (YAIO2079)**
    - neuer PPL-Exporter (YAIO2080)
    - Erweiterung des Markdown um Freemind (YAIO2081)
    - BaseRef in Html-Export (YAIO2077)
- **GUI (YAIO2047)**
    - Einbindung von Toast-Messages (Fehler, Infos usw.) (YAIO2062)
    - Layoutverbesserungen für Exporte (YAIO2065)
    - Download/SourceView-Links für Diagramme (YAIO2066)
    - TOC (YAIO2067)
    - Gantt-Datum auf +-3Monate von heute setzen (YAIO2069)
    - Bugfix Styles/Druckversion (YAIO2070)
- **Code-Verbesserungen (YAIO2048)**
    - YAIO-Caller Bugfix Encoding + FileOutput-Option (YAIO2071)
    - Html-Ids für Markdown-Elemente eindeutig vergeben (YAIO2072)
    - Im/Ex/Converter auf einheitliche Code-Basis (YAIO2073)
    - Refactoring JS (YAIO2074)
    - Codeclean: static DateFormat-Fields (YAIO2075)
    - Angular-Refactoring globale Util-Funktionen (YAIO2076)
    - YAIO-Caller Codeverbesserung (YAIO2078)

## Changelog for release: feature-YAIO1980-improvements-201502
- **GUI (YAIO1981)**
    - Export als Jira oder Plaintext (YAIO2031)
    - Statistiken + Tree-Expander nur anzeigen, wenn Node children hat (YAIO1676)
    - expander nur anzeigen, wenn children vorhanden (YAIO1677)
    - Statistik anzeigen (YAIO2014)
    - Language-Switcher (Übersetzung nach en) (YAIO1625)
    - Einbindung von Diagrammen (YAIO2017)
    - Standalone-Wysiwyg-Editor (YAIO2027)
    - statische Startseiten (YAIO2030)
- **Code-Verbesserungen (YAIO1983)**
    - Alle Shellscipte in Hauptprojekt uebernehmen (YAIO1853)
- **Security-Isuses (YAIO1984)**
    - Html und Markdown (YAIO1985)
- **Erweiterungen (YAIO1960)**
    - Absicherung über User-Modul (YAIO1970)
        - Konfiguration von Apache-Domains für eigene Session-Coockie (YAIO2019)
        - YAIO Basisabsicherung (YAIO2023)
        - Absicherung: WS nur nach Login abrufbar (YAIO1709)
    - Admin-Endpunkte (YAIO1986)
    - Clients für Remote-Calls (YAIO1987)    

## Changelog for milestone: 201501 (YAIO1931)
- **GUI (YAIO1932)**
    - Markdown für Html-Export (YAIO1961)
    - SyntaxHighLighting für Html-Export (YAIO1939)
    - Aufbereitung Druckversion (YAIO1952)
    - DetailBlock mit Metadaten: Create/Change/metaNodeNumer... (mit Toggler) (YAIO1469)
    - Wyswyg-Editor (YAIO1969)
    - Vorlesefunktion (YAIO1934)
- **Code-Verbesserungen (YAIO1953)**
    - Improve app-naming (YAIO1954)
    - Actuator mit Managementinfos (YAIO1955)

## Changelog for milestone: 201411-12 (YAIO255)
- **GUI (YAIO1573)**
    - bei neuer InfoNode, UrlRes-Node Standardtyp vorbelegen (YAIO1845)
    - Formatierung der Beschreibung (YAIO1856)
    - Preview-Fenster (YAIO1904)
    - Verbesserung Druckversion (YAIO1905)
    - Verschärfung der Vailidierungsregeln (YAIO1816)
- **Code-Verbesserungen (YAIO1576)**
    - eigene JS-Klassen (YAIO1250)

## Changelog for milestone: 201410 SpringRoo-Web (YAIO642)
- **neue Funktionen**
    - Implementierung eines RESTfull webservice zum Abruf Version1 (YAIO1169)
    - Implementierung eines Javascript-Ports (YAIO1200)

## Changelog for milestone: 201409
- **Implementierung SpringRoo-Persistence (Step1) (YAIO1140)**
    - Implementierung Port Persistence mit Roo (YAIO727)
    - Performance-Verbesserung Startup (YAIO1117)
    - DB-Config in Parameterfile auslagern (YAIO1142)
    
## Changelog for milestone: 201404
- **Neu-Implementierung mit Spring Roo (YAIO445)**
    - Testprojekt aufsetzen (YAIO446)
    - YAIO zur Übernahme integriert (YAIO448)
    - Modell (YAIO449)
    - Port Importer Allg (YAIO469)
    - Port Exporter Allg (YAIO479)
    - Anwendungs-Umgebung aufsetzen (YAIO588)
    - Port Prasentation Statisch (YAIO489)

## Changelog for milestone: 201401 (YAIO142)
- **Neue Features**
    - Darstellungs-Filter V1 (YAIO143)
    - Systemdaten speichern (YAIO146)
    - Metadaten speichern (YAIO149)
    - Diff-Script -> start winmerge wenn Diffs vorhanden (YAIO206)
    - CYGWIN-Warnungen ausschalten (YAIO207)
    - Url-Ressourcen als Typ (YAIO153)
    - Neuer Typ Template (YAIO211)
    - Datumswerte bei Plan+Ist mit optionaler Zeitangabe versehen (insbesondere für Termine) (YAIO252)
    - Export als CSV (TAB) (YAIO253)
    - Export als JSON (YAIO254)
    - Volltextsuche im Javascript V1 (YAIO152)


