# Installation

There are several ways to install your own D'Artagnan-Box.

## A) linux: use installer
get the installer and start it on an system

    git clone https://github.com/das-praktische-schreinerlein/yaioinstaller.git
    sudo chmod 555 -R ./yaioinstaller
    sudo ./yaioinstaller/install-yaiodemo/uninstall-allsteps-all-root-UBUNTU.sh
    sudo ./yaioinstaller/install-yaiodemo/install-allsteps-all-root-UBUNTU.sh

## B) linux/windows (with vagrant): use installer 
download installer

    mkdir yaiodemo
    cd yaiodemo
    mkdir datatransfer
    cd datatransfer
    git clone https://github.com/das-praktische-schreinerlein/yaioinstaller.git
    cd ..

start a new vagrantbox

    vagrant up
    vagrant ssh

run installer on new vagrantbox

    cd /vagrant/datatransfer/
    sudo chmod 555 -R ./yaioinstaller
    sudo ./yaioinstaller/install-yaiodemo/uninstall-allsteps-all-root-UBUNTU.sh
    sudo ./yaioinstaller/install-yaiodemo/install-allsteps-all-root-UBUNTU.sh

call your browser: http://IpOfTheVagrantBox:8666/demo.html
    
## C) Install from git (windows or linux with existing java1.7, maven, nodejs 10.XX)

### Requires
- for building
    - java 1.7
    - maven
    - node
    - IDE (I built it with eclipse)
- to use, take a look at http://www.your-all-in-one.de/yaio/static/lepetithow#h3_YAIO716
    - java
    - a browser
    - freemind

### Step1: get sources

get sources

    cd YOURBASEDIR
    git clone https://github.com/das-praktische-schreinerlein/your-all-in-one.git
    cd your-all-in-one/

Rechte

    chmod 755 sbin -R
    mkdir logs

install karma local

    npm install karma --save-dev
    npm install karma-phantomjs-launcher karma-jasmine karma-qunit karma-chrome-launcher karma-firefox-launcher plugin --save-dev

### Step2: baseconfigure and build
config/applicationContext.xml

    <context:property-placeholder location="YOURAPPBASEDIR/config/application.properties"/>

config/application.properties

    config.spring.applicationconfig.path=YOURAPPBASEDIR/config/applicationContext.xml
    port: 8666
    server: 0.0.0.0
    yaio.exportcontroller.examples.location=file:///YOURAPPBASEDIR/resources/examples/
    databaseHsqldb.url=jdbc\:hsqldb\:file\:YOURAPPBASEDIR/var/hsqldb/yaio;shutdown\=true;hsqldb.write_delay\=false;
    yaio.exportcontroller.replace.baseref=/

karma-executable in pom.xml

    <karmaExecutable>${basedir}/node_modules/karma/bin/karma</karmaExecutable>

karma-config karma.yaio.conf.js

    browsers: ['PhantomJS']

delete last empty lines in database-file

    var/hsqldb/yaio.script

generate without tests

    mvn -Dmaven.test.skip=true package

### Step3: finish config

configure accounts in config/security-apiusers.properties

Loglevel: config/log4j.properties

    log4j.appender.R.File=logs/yaio.log

### Step4: run
start

    cd YOURBASEDIR
    sbin\start-yaioapp.bat

call your browser: http://localhost:8666/demo.html

### Step5: Example for the batches

run example 

    cd YOURAPPBASEDIR
    src\test\testproject\gen-planung.bat

Take a look at the Wiki, Mindmaps, Excel, html, ICal...
