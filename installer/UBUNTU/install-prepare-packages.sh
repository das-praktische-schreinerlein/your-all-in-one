#!/bin/bash
###################
## prepare system for yaio-install
###################
SCRIPT_DIR=$(dirname $0)/
SCRIT_BASE_DIR=${SCRIPT_DIR}/../

# load utils
. ${SCRIT_BASE_DIR}/utils.sh

# set noninteractive and updatepackages
export DEBIAN_FRONTEND=noninteractive \
    && apt-get update -q \
    && apt-get install -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" \
        curl \
        ghostscript \
        graphviz \
        less \
        sudo \
        telnet \
        unzip \
        wget \
        xfonts-base \
        xfonts-75dpi \
    && apt-get autoremove -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" \
    && rm -rf /var/lib/apt/lists/*

# install wkhtmltopdf for yaio-webshot-service
wget http://download.gna.org/wkhtmltopdf/0.12/0.12.2.1/wkhtmltox-0.12.2.1_linux-trusty-amd64.deb \
    && dpkg -i wkhtmltox-0.12.2.1_linux-trusty-amd64.deb \
    && apt-get -y -f install \
    && dpkg -i wkhtmltox-0.12.2.1_linux-trusty-amd64.deb \
    && rm wkhtmltox-0.12.2.1_linux-trusty-amd64.deb;
wget http://download.gna.org/wkhtmltopdf/0.12/0.12.2.1/wkhtmltox-0.12.2.1_linux-jessie-amd64.deb \
    && dpkg -i wkhtmltox-0.12.2.1_linux-jessie-amd64.deb \
    && apt-get -y -f install \
    && dpkg -i wkhtmltox-0.12.2.1_linux-jessie-amd64.deb \
    && rm wkhtmltox-0.12.2.1_linux-jessie-amd64.deb;

# cleanup
apt-get autoremove -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold"
