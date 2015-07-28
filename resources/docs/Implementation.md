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
    - Persistence: [Spring-roo](https://github.com/spring-projects/spring-roo)
    - Persistence: [Hibernate](https://github.com/hibernate/)

## Java-Datatransfer-Tools
- functionality
    - Datatransfer
    - Converter
- packages
    - de.yaio.datatransfer.exporter: Exporter
    - de.yaio.datatransfer.importer: Importer
    - de.yaio.extension.datatransfer: Extensions for converter to different formats
- use external libs
    - Markdown-Formatter: [Markdown4j](https://code.google.com/p/markdown4j/)
    - Excel Ex/Import: [Apache Poi](https://github.com/apache/poi)

## Java-Frontend
- functionality
    - Rest-API
    - WebFrontend Converter
    - Admin-WebFrontend
- packages
    - de.yaio.rest.controller: controller for the webservices
    - de.yaio.webapp: web-application-sources
- use external libs
    - WebFramework [Spring-boot](https://github.com/spring-projects/spring-boot)
    - Security [Spring Security](https://github.com/spring-projects/spring-security)

## JS-ExplorerApp
- functionality
    - Explorer-App with TreeView, Editor, Wysiwyg-Markdown-Editor...
- packages
    - src/main/web/yaio-explorerapp/
- use external Libs
    - App-Framwork [AngularJS](https://angularjs.org/)
    - Layout-Framework [JQuery](https://github.com/jquery/jquery)
    - Html-Editor [Ace](https://github.com/ajaxorg/ace-builds)
    - Explorer-Visualtsation [fancytree](https://github.com/mar10/fancytree)
    - GUI-Features [JQuery-UI](https://github.com/jquery/jquery-ui)
    - Image-Show [Slimbox2](http://www.digitalia.be/software/slimbox2)
    - Toast-Messages [Toastr](https://github.com/CodeSeven/toastr)
    - DOM-Manipulation [findAndReplaceDOMText](https://github.com/padolsey/findAndReplaceDOMText)
    - TOC [Strapdown TOC](https://github.com/ndossougbe/strapdown)
    - Multilanguage for Tooltipps [JQuery-Lang](https://github.com/coolbloke1324/jquery-lang-js)
    - Multilanguage for Angular-Fields [Angular-Translate](https://github.com/angular-translate/)
    
## JS-Formatter
- functionality
    - formatting of all markdown-sources: Markdown-Formatter, Diagramm-Formatter, Checklists, Wysiwyg-Markdown-Editor
- packages
    - src/main/web/yaio-explorerapp/js/layout/
    - src/main/web/yaio-explorerapp/js/wysiwyg/
    - src/main/web/yaio-explorerapp/wysiwyg.html
- use external Libs
    - Layout-Framework [JQuery](https://github.com/jquery/jquery)
    - Html-Editor [Ace](https://github.com/ajaxorg/ace-builds)
    - Markdown-Parser+Formatter [Marked](https://github.com/chjj/marked)
    - Syntax-Highlighting [highlight.js](https://highlightjs.org/)
    - Web-Diagrams [mermaid](https://github.com/knsv/mermaid)
    - Freemmind-Browser [Freemind Flash-Browser](http://freemind.sourceforge.net/wiki/index.php/Flash_browser)


