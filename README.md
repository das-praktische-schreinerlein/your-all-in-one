your-all-in-one - Le petit D'Artagnan
=====================

# Desc
"Your-All-In-One" or "Le petit D'Artagnan" is the prototype of my 
collaboration-softwareproject: "D'Artagnan - Dein persönlicher Software-Musketier".

More information about the prototype can you find at http://www.your-all-in-one.de/yaio/static/lepetit 

# TODO for me
- [ ] documentation
- [ ] use and optimize it :-)

# History and milestones
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
   - planing and documentation with a javascript-wiki-editor/parser
- 2010
   - initial version with excel for private project-planing
   - used at private projects and some projects at work

# Requires
- for building
   - maven
   - spring-roo
   - IDE (I built it with eclipse)
- for use, take a look at http://www.your-all-in-one.de/yaio/static/lepetithow#h3_YAIO716
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

- run maven 
```bat
cd d:\public_projects\yaio
mvn compile
mvn org.apache.maven.plugins:maven-assembly-plugin:assembly
```

# Configure
- update pathes in 
   - config/config-yaio.bat

# Example
- run example 
```bat
cd d:\public_projects\yaio
src\test\testproject\gen-planung.bat
```
Take a look at the Wiki, Mindmaps, Excel, html, ICal...

# License
```
/**
 * @author Michael Schreiner <ich@michas-ausflugstipps.de>
 * @category publishing
 * @copyright Copyright (c) 2010-2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
```
