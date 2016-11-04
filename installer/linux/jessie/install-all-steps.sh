#!/bin/bash
###################
## install yaio-app
###################

export YAIO_USER=yaio
export YAIO_HOME=/opt/yaio_home/
export YAIO_RUN_DIR=${YAIO_HOME}/
export SCRIPT_DIR=$(dirname $0)/
export LINUX_BASE_DIR=${SCRIPT_DIR}/../
export BASE_DIR=${LINUX_BASE_DIR}/../../
ME=`basename "$0"`

PWD=`pwd`

chmod 755 -R ${LINUX_BASE_DIR}/*.sh

echo "$ME: prepare install"
${SCRIPT_DIR}/basecommon-prepare-base-packages.sh
${SCRIPT_DIR}/install-prepare-packages.sh

echo "$ME: start setup-distribution for ${YAIO_RUN_DIR}"
${LINUX_BASE_DIR}/setup-distribution.sh ${YAIO_RUN_DIR}

# create user
echo "$ME: add user ${YAIO_USER}"
useradd -ms /bin/bash -d ${YAIO_HOME} ${YAIO_USER}

# install
echo "$ME: start install"
chown -R yaio ${YAIO_HOME} && chmod +x ${YAIO_RUN_DIR}/sbin/*.sh ${YAIO_RUN_DIR}/config/*.sh ${YAIO_RUN_DIR}/installer/**/*.sh
${YAIO_RUN_DIR}/installer/linux/configure-yaio.sh

# setup runlevel
echo "$ME: setup runlevel"
cp ${YAIO_RUN_DIR}/sbin/yaio-app.sh /etc/init.d/
chown root /etc/init.d/yaio-app.sh
chmod 500 /etc/init.d/yaio-app.sh
cd /etc/init.d
update-rc.d yaio-app.sh defaults

cd $PWD
