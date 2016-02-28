# Implementation

## Java-Backend
- functionality
    - Persistence
    - Validation
    - Business-Logic
- packages:
    - de.yaio.core.datadomain: DataDomains with the interfaces for the business-logic
    - de.yaio.core.datadomainservice: dataservices for the business-logic of DataDomains
    - de.yaio.core.dbservice: services for the domain-beans
    - de.yaio.core.node: beans with the implementation of DataDomain-interfaces
    - de.yaio.core.nodeservice: dataservices for the business-logic of domains
- use external libs
    - Persistence, Validation...: [Spring-Framework](https://github.com/spring-projects/spring-framework)
    - Persistence: [Hibernate](https://github.com/hibernate/)
    - Persistence: [Spring-roo](https://github.com/spring-projects/spring-roo)

## Java-Datatransfer-Tools
- functionality
    - Datatransfer
    - Converter
- packages
    - de.yaio.datatransfer.exporter: Exporter
    - de.yaio.datatransfer.importer: Importer
    - de.yaio.extension.datatransfer: Extensions for converter to different formats
- use external libs
    - Encoding detector: [icu4j](http://icu-project.org/)
    - Excel Ex/Import: [Apache Poi](https://github.com/apache/poi)
    - Markdown-Formatter: [Markdown4j](https://code.google.com/p/markdown4j/)

## Java-Frontend
- functionality
    - Rest-API
    - WebFrontend Converter
    - Admin-WebFrontend
- packages
    - de.yaio.rest.controller: controller for the webservices
    - de.yaio.webapp: web-application-sources
- use external libs
    - Security: [Spring Security](https://github.com/spring-projects/spring-security)
    - WebFramework: [Spring-boot](https://github.com/spring-projects/spring-boot)

## JS-ExplorerApp
- functionality
    - Explorer-App with TreeView, Editor, Wysiwyg-Markdown-Editor...
- [yaio-explorerapp on Github](https://github.com/das-praktische-schreinerlein/yaio-explorerapp)
- use external Libs
    - App-Framework: [AngularJS](https://angularjs.org/)
    - App-Routing: [Angular-Route](https://github.com/angular/bower-angular-route)
    - DOM-Manipulation: [findAndReplaceDOMText](https://github.com/padolsey/findAndReplaceDOMText)
    - Explorer-Visualisation: [fancytree](https://github.com/mar10/fancytree)
    - Html-Editor: [Ace](https://github.com/ajaxorg/ace-builds)
    - Image-Show: [Slimbox2](http://www.digitalia.be/software/slimbox2)
    - Layout-Framework: [JQuery](https://github.com/jquery/jquery)
    - Multilanguage for Angular-Fields: [Angular-Translate](https://github.com/angular-translate/)
    - Multilanguage for Angular-Fields from Static-Files: [Angular-Translate-Loader-Static-Files](https://github.com/angular-translate/bower-angular-translate-loader-static-files)
    - Multilanguage for Tooltipps: [JQuery-Lang](https://github.com/coolbloke1324/jquery-lang-js)
    - SEO-Metadata: [Angular-Update-Meta](https://github.com/jvandemo/angular-update-meta)
    - Toast-Messages: [Toastr](https://github.com/CodeSeven/toastr)
    - TOC: [Strapdown TOC](https://github.com/ndossougbe/strapdown)
    - UI-Features: [JQuery-UI](https://github.com/jquery/jquery-ui)
    - UI-Features ContextMenu: [UI-Contextmenu](https://github.com/mar10/jquery-ui-contextmenu)
    - UI-Features Date/TimePicker: [JQuery-UI-Timepicker](https://github.com/trentrichardson/jQuery-Timepicker-Addon)
    - UI-Features Fat Selectboxes: [Select2](https://github.com/select2/select2)
    - UI Search Pagination: [Angular-Paging](https://github.com/brantwills/Angular-Paging)

## Wysiwyg-Markdown-Editor
- functionality
    - online-markdown-editor: Markdown-Formatter, Diagramm-Formatter, Checklists, Wysiwyg-Markdown-Editor
- [ymf on Github](https://github.com/das-praktische-schreinerlein/your-markdown-fellow)
- use external Libs
    - Freemmind-Browser: [Freemind Flash-Browser](http://freemind.sourceforge.net/wiki/index.php/Flash_browser)
    - Html-Editor: [Ace](https://github.com/ajaxorg/ace-builds)
    - Layout-Framework: [JQuery](https://github.com/jquery/jquery)
    - Markdown-Parser+Formatter: [Marked](https://github.com/chjj/marked)
    - Syntax-Highlighting: [highlight.js](https://highlightjs.org/)
    - Web-Diagrams: [mermaid](https://github.com/knsv/mermaid)
    - Web-Diagrams: [PlantUML](http://plantuml.com/)

## Java-Services

### yaio-commmons
- functionality
    - common functions and utils for yaio
- [yaio-commons on Github](https://github.com/das-praktische-schreinerlein/yaio-commons)

### yaio-pom-webstarter
- functionality
    - starter pom for spring-boot-webapps
- [yaio-pom-webstarter on Github](https://github.com/das-praktische-schreinerlein/yaio-pom-webstarter)

### yaio-plantuml-service
- functionality
    - webservie to create plantuml-diagrams from source
    - inspired by [PlantUML Server](https://github.com/plantuml/plantuml-server)
- [yaio-plantuml-service on Github](https://github.com/das-praktische-schreinerlein/yaio-plantuml-service)
- use external libs
    - [Plantuml-Core](https://github.com/plantuml/plantuml)

### yaio-metaextract-service
- functionality
    - webservice to extract text and metadata from uploaded files or urls
- [yaio-metaextract-service on Github](https://github.com/das-praktische-schreinerlein/yaio-metaextract-service)
- use external libs
    - [Apache Tika](https://tika.apache.org/1.11/gettingstarted.html)
    - [Tess4j](http://tess4j.sourceforge.net/)
    - [Tesseract](https://github.com/tesseract-ocr/tesseract)

### yaio-webshot-service
- functionality
    - webservice to make a webshot of the url as png or pdf
- [yaio-webshot-service on Github](https://github.com/das-praktische-schreinerlein/yaio-webshot-service)
- use external libs
    - [Wkhtmltopdf](https://github.com/wkhtmltopdf/wkhtmltopdf)

### yaio-dms-service
- functionality
    - webservice to store and retrieve documents with simple dms-functions
- [yaio-dms-service on Github](https://github.com/das-praktische-schreinerlein/yaio-dms-service)

## Development
- functionality
    - build and support-tools
- packages
    - Build  
        - Global+Java with Maven: pom.xml
        - JSApp with Grunt: Gruntfile.js
    - E2E-Tests Protractor: protractor.yaio.conf.js
    - JS-Unit-Tests Karma: karam.yaio.conf.js
    - Package-Manager
        - JS-App Bower: bower.js
        - Node npm: package.json
- use external Libs
    - **Node**
        - Dev-Stack: [Nodejs](https://nodejs.org)
        - Frontend-Packagemanager: [Bower](http://bower.io/)
        - Packagemanager: [NPM](https://www.npmjs.com/)
        - Taskrunner: [Grunt](http://gruntjs.com/)
        - Test-Framework: [Jasmine](http://jasmine.github.io/)
        - Test-Runner: [Karma](http://karma-runner.github.io/)
        - Test-Browser Headless: [Phantomjs](http://phantomjs.org/)
        - Test-Framework e2e: [Protractor](https://angular.github.io/protractor/#/)
        - Test-Framework Browser automation: [Selenium](http://www.seleniumhq.org/)
    - **Java**
        - Build: [Maven](https://maven.apache.org/)
        - Codeanalysis codestyle: [Checkstyle](https://github.com/checkstyle/checkstyle)
        - Codeanalysis possible bugs: [Findbugs](http://findbugs.sourceforge.net/)
        - Codeanalysis possible bugs: [Pmd](http://pmd.sourceforge.net/snapshot/pmd-java/)
        - Codeanalysis dependencies:[Dependency-Analyzer](http://www.dependency-analyzer.org/)
        - Codeanalysis dependency documentation: [Plantuml-Dependency-Analyser](http://plantuml-depend.sourceforge.net/download/download.html)
        - Diagrams: [Plantuml-Doclet](http://de.plantuml.com/doclet.html)
        - Test-Framework: [Junit](http://junit.org/)
        - RESTful-API-Interface documentation: [Springfox Swagger](http://springfox.github.io/springfox/)
    - **Virtualisation**
        - Docker: [Docker](https://www.docker.com/)
        - Vagrant: [Vagrant](https://www.vagrantup.com/)
