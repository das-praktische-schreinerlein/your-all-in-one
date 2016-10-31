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
apt-get install -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" maven

# install nodejs 10
apt-cache showpkg nodejs
apt-get remove -q -y nodejs
curl -sL https://deb.nodesource.com/setup_0.10 | sudo bash -
apt-get install -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" nodejs

# install git
apt-cache showpkg git
apt-get install -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" git

# install chrome+xvfb for e2e-tests
apt-get install -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" chromium-browser
apt-get install -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" xvfb

# cleanup
apt-get autoremove -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold"