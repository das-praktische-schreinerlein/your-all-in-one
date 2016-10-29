#!/bin/bash
###################
## prepare system for yaio-install
###################
SCRIPT_DIR=$(dirname $0)/
LINUX_BASE_DIR=${SCRIPT_DIR}/../

# load utils
. ${LINUX_BASE_DIR}/utils.sh

# set timezone
echo 'Europe/Berlin' | tee /etc/timezone > /dev/null
dpkg-reconfigure -f noninteractive tzdata

# set noninteractive and updatepackages
export DEBIAN_FRONTEND=noninteractive
apt-get update -q

# install maven
apt-get -y remove maven
apt-get -y install gdebi
wget http://ppa.launchpad.net/natecarlson/maven3/ubuntu/pool/main/m/maven3/maven3_3.2.1-0~ppa1_all.deb
gdebi --non-interactive maven3_3.2.1-0~ppa1_all.deb
ln -s /usr/share/maven3/bin/mvn /usr/bin/maven
ln -s /usr/share/maven3/bin/mvn /usr/bin/mvn

# install nodejs 10
apt-cache showpkg nodejs
apt-get remove node
apt-get install -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" nodejs=0.10.25~dfsg2-2ubuntu1
apt-get install -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" npm
# link because of collision with node-package
apt-get remove node
ln -s /usr/bin/nodejs /usr/sbin/node

# install git
apt-cache showpkg git
apt-get install -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" git

# install chrome+xvfb for e2e-tests
apt-get install -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" chromium-browser
apt-get install -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" xvfb

# cleanup
apt-get autoremove -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold"