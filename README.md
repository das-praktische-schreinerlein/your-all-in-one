your-all-in-one - Le petit D'Artagnan
=====================

# Desc
"Your-All-In-One" or "Le petit D'Artagnan" is the prototype of my 
collaboration-softwareproject: "D'Artagnan - Dein persönlicher Software-Musketier".

More information about the prototype at http://www.your-all-in-one.de/yaio/static/lepetit 

# TODO for me
- [ ] update the documentation on http://www.your-all-in-one.de/yaio/static/lepetit  (new features, howto...)
- [ ] code-documentation
- [ ] code review: configuration, minor layout-bugs
- [ ] new feature: fulltextsearch with lucene and solr
- [ ] new feature: new nodetypes (contact, call, bug, document..)
- [ ] new feature: document-management
- [ ] new feature: contact-management
- [ ] use and optimize it :-)

# History and milestones
- 2014
   - added markdown to format desc with automatic synatx-hihglighting for code-blocks
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

# Requires
- for building
   - maven
   - spring-roo
   - IDE (I built it with eclipse)
- to use, take a look at http://www.your-all-in-one.de/yaio/static/lepetithow#h3_YAIO716
   - java
   - notepadd++
   - freemind
   - firefox
   - excel
   - winmerge
   - cygwin

# Install
- save the project to 
```bat
d:\public_projects\yaio
```

- import project to Eclipse

# Configure
- update config in 
   - config/config-yaio.bat (pathes to external progs, port)
   - config/applicationContext.xml
   - config/application.properties
   - src/test/java/de/yaio/rest/controller/BaseNodeRestControllerTest.java (its bullshit to configure it here, but be sure it ion my todo-list)

# Generate
- run maven 
```bat
cd d:\public_projects\yaio
mvn compile
mvn package
```

# Enyoy
- run webview
```bat
cd d:\public_projects\yaio
sbin\start-yaioapp.bat
```

# Example for the batches
- run example 
```bat
cd d:\public_projects\yaio
src\test\testproject\gen-planung.bat
```
Take a look at the Wiki, Mindmaps, Excel, html, ICal...

# Thanks to
- https://code.google.com/p/markdown4j/
- https://github.com/ajaxorg/ace-builds
- https://github.com/angular-translate/
- https://github.com/apache/maven
- https://github.com/apache/poi
- https://github.com/coolbloke1324/jquery-lang-js
- https://github.com/jquery/jquery
- https://github.com/jquery/jquery-ui
- https://github.com/knsv/mermaid
- https://github.com/mar10/fancytree
- https://github.com/spring-projects/spring-boot
- https://github.com/spring-projects/spring-framework
- https://github.com/spring-projects/spring-roo
- https://highlightjs.org
 

# License
```
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
```
