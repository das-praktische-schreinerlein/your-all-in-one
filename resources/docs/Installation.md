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
    cd ..

start a new vagrantbox (set memory to 1GB in Vagrantfile)

    vagrant init ubuntu/trusty64
    vagrant up --provider virtualbox
    vagrant ssh

run installer on new vagrantbox

    cd /vagrant/datatransfer/
    git clone https://github.com/das-praktische-schreinerlein/yaioinstaller.git
    sudo chmod 555 -R ./yaioinstaller
    sudo ./yaioinstaller/install-yaiodemo/uninstall-allsteps-all-root-UBUNTU.sh
    sudo ./yaioinstaller/install-yaiodemo/install-allsteps-all-root-UBUNTU.sh

call your browser: http://IpOfTheVagrantBox:8666/demo.html
    
## C) Install from git (windows or linux with existing java1.7, maven)

### Requires
- for building
    - java 1.7
    - maven
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

### Step2: baseconfigure and build

config/yaio-application.properties

    port: 8666
    server: 0.0.0.0
    yaio.exportcontroller.examples.location=file:///YOURAPPBASEDIR/resources/examples/
    databaseHsqldb.url=jdbc\:hsqldb\:file\:YOURAPPBASEDIR/var/hsqldb/yaio;shutdown\=true;hsqldb.write_delay\=false;
    yaio.exportcontroller.replace.baseref=/

delete last empty lines in database-file

    var/hsqldb/yaio.script

generate without tests

    mvn package

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
