your-all-in-one - Le petit D'Artagnan
=====================

# Desc
"Your-All-In-One" or "Le petit D'Artagnan" is the prototype of my 
collaboration-softwareproject: "D'Artagnan - Dein persönlicher Software-Musketier".

More information about the prototype will you find at http://www.your-all-in-one.de/yaio/static/lepetit 

# TODO for me
- [ ] documentation
- [ ] use and optimize it :-)

# Requires
- for building
   - maven
   - spring-roo
   - IDE (I built it with eclipse)
- to use (look at http://www.your-all-in-one.de/yaio/static/lepetithow#h3_YAIO716
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
