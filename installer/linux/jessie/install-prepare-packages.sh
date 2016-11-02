#!/bin/bash
###################
## prepare system for yaio-install
###################
SCRIPT_DIR=$(dirname $0)/
LINUX_BASE_DIR=${SCRIPT_DIR}/../
ME=`basename "$0"`

# load utils
. ${LINUX_BASE_DIR}/utils.sh

echo "$ME: install packages"

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
        wkhtmltopdf \
    && apt-get autoremove -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" \
    && rm -rf /var/lib/apt/lists/*

# cleanup
apt-get autoremove -q -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold"
