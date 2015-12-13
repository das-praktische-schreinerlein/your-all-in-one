your-all-in-one - Le petit D'Artagnan
=====================

# Desc
"Your-All-In-One" or "Le petit D'Artagnan" is the prototype of my 
collaboration-softwareproject: "D'Artagnan - Dein persönlicher Software-Musketier".

More information about the prototype at [http://www.your-all-in-one.de/yaio/static/lepetit](http://www.your-all-in-one.de/yaio/static/lepetit )
# Installation
- **see common details on [resources/docs/Installation.md](resources/docs/Installation.md)**

# Todos for me
- [ ] update the documentation on [http://www.your-all-in-one.de/yaio/static/lepetit](http://www.your-all-in-one.de/yaio/static/lepetit) (new features, howto...)
- [ ] code-documentation
- [ ] code review: configuration, minor layout-bugs
- [ ] new feature: new nodetypes (contact, call, bug, document..)
- [ ] new feature: contact-management
- [ ] use and optimize it :-)
- **and a lot more to implement - take a look at [resources/docs/Roadmap.md](resources/docs/Roadmap.md)**

# History and milestones
- **see details on [resources/docs/Changelog.md](resources/docs/Changelog.md)**
- 2015
   - improved layout 
   - secured app with user-login 
   - added Admin-Scripts, Commandline-Interface for the App
   - added new exports (PPL, Jira, Text)
   - added multilanguage-support
   - added spech-synthesizer to read the text
   - added mindmap, state-diagramms as markdown-extension
   - added checklist, app-propagator, installer, searchpreview
   - added e2e-tests
   - improved code/package structure
   - improved deployment process (documentation, packaging...)
   - added plantuml-diagramm-extension
   - added webshot, metadata-extractor, document-management, fileupload...

- 2014
   - added markdown to format desc with automatic synatx-highlighting for code-blocks
   - add searchview
   - added yaio-explorer as frontendview
   - added RESTfull-WS
   - added first persistence-functions and support for database as datasource (mysql, hsqldb)
- 2014 
   - prepared the prototype for going public (documentation...) 
   - separated the public-version
- 2014 
   - reimplementation with spring-roo and new model 
- 2013
   - new functions and exports to Mindmap, ICal, Html...
   - used at all private projects and private management
- 2012
   - java-progs for parsing and converting wiki to export-media
- 2011
   - planning and documentation with a javascript-wiki-editor/parser
- 2010
   - initial version with excel for private project-planning
   - used at private projects and some projects at work


# Implementation
- **see details on [resources/docs/Implementation.md](resources/docs/Implementation.md)**

# Thanks to
- **Build-Tools**
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
- **Java-Core-Frameworks**
    - Encoding detector: [icu4j](http://icu-project.org/)
    - Excel Ex/Import: [Apache Poi](https://github.com/apache/poi)
    - Markdown-Formatter: [Markdown4j](https://code.google.com/p/markdown4j/)
    - Persistence, Validation...: [Spring-Framework](https://github.com/spring-projects/spring-framework)
    - Persistence: [Hibernate](https://github.com/hibernate/)
    - Persistence: [Spring-roo](https://github.com/spring-projects/spring-roo)
    - Security: [Spring Security](https://github.com/spring-projects/spring-security)
    - WebFramework: [Spring-boot](https://github.com/spring-projects/spring-boot)
- **More Java-libs and progs**
    - [Plantuml-Core](https://github.com/plantuml/plantuml)
    - [Apache Tika](https://tika.apache.org/1.11/gettingstarted.html)
    - [Tess4j](http://tess4j.sourceforge.net/)
    - [Tesseract](https://github.com/tesseract-ocr/tesseract)
    - [Wkhtmltopdf](https://github.com/wkhtmltopdf/wkhtmltopdf)
- **JS-Code-Frameworks**
    - App-Framework: [AngularJS](https://angularjs.org/)
    - Layout-Framework: [JQuery](https://github.com/jquery/jquery)
- **JS-GUI**
    - App-Routing: [Angular-Route](https://github.com/angular/bower-angular-route)
    - DOM-Manipulation: [findAndReplaceDOMText](https://github.com/padolsey/findAndReplaceDOMText)
    - Explorer-Visualisation: [fancytree](https://github.com/mar10/fancytree)
    - Html-Editor: [Ace](https://github.com/ajaxorg/ace-builds)
    - Image-Show: [Slimbox2](http://www.digitalia.be/software/slimbox2)
    - SEO-Metadata: [Angular-Update-Meta](https://github.com/jvandemo/angular-update-meta)
    - Toast-Messages: [Toastr](https://github.com/CodeSeven/toastr)
    - TOC: [Strapdown TOC](https://github.com/ndossougbe/strapdown)
    - UI-Features: [JQuery-UI](https://github.com/jquery/jquery-ui)
    - UI-Features ContextMenu: [UI-Contextmenu](https://github.com/mar10/jquery-ui-contextmenu)
    - UI-Features Date/TimePicker: [JQuery-UI-Timepicker](https://github.com/trentrichardson/jQuery-Timepicker-Addon)
    - UI-Features Fat Selectboxes: [Select2](https://github.com/select2/select2)
    - UI Search Pagination: [Angular-Paging](https://github.com/brantwills/Angular-Paging)
- **JS-Formatter**
    - Freemmind-Browser: [Freemind Flash-Browser](http://freemind.sourceforge.net/wiki/index.php/Flash_browser)
    - Html-Editor: [Ace](https://github.com/ajaxorg/ace-builds)
    - Layout-Framework: [JQuery](https://github.com/jquery/jquery)
    - Markdown-Parser+Formatter: [Marked](https://github.com/chjj/marked)
    - Syntax-Highlighting: [highlight.js](https://highlightjs.org/)
    - Web-Diagrams: [mermaid](https://github.com/knsv/mermaid)
    - Web-Diagrams: [PlantUML](http://plantuml.com/)
- **JS-Multilanguage**
    - Multilanguage for Angular-Fields: [Angular-Translate](https://github.com/angular-translate/)
    - Multilanguage for Angular-Fields from Static-Files: [Angular-Translate-Loader-Static-Files](https://github.com/angular-translate/bower-angular-translate-loader-static-files)
    - Multilanguage for Tooltipps: [JQuery-Lang](https://github.com/coolbloke1324/jquery-lang-js)
- **Media**
    - [paulrobertlloyd.com](http://paulrobertlloyd.com/2009/06/social_media_icons/)


# License
    /**
     * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category collaboration
     * @copyright Copyright (c) 2010-2014, Michael Schreiner
     * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     *
     * This Source Code Form is subject to the terms of the Mozilla Public
     * License, v. 2.0. If a copy of the MPL was not distributed with this
     * file, You can obtain one at http://mozilla.org/MPL/2.0/.
     */
