#
# Creates a docker container with Yaio
#
FROM java:openjdk-8u45-jdk

MAINTAINER Michael Schreiner <michael.schreiner@your-it-fellow.de>

USER root

##########################################################
# Prepare yaio
##########################################################
RUN useradd -ms /bin/bash -d /opt/yaio_home yaio
RUN mkdir -p /opt/yaio_home/installer/linux
COPY build/installer/linux/ /opt/yaio_home/installer/linux/
RUN /opt/yaio_home/installer/linux/jessie/install-prepare-packages.sh

##########################################################
# copy and configure build
##########################################################
COPY build/ overrides/OVERWRITE/ /opt/yaio_home/
RUN chown -R yaio /opt/yaio_home && chmod +x /opt/yaio_home/sbin/*.sh /opt/yaio_home/config/*.sh /opt/yaio_home/installer/**/*.sh
RUN /opt/yaio_home/installer/linux/configure-yaio.sh

##########################################################
# Run-options
##########################################################
EXPOSE 8666
ENTRYPOINT ["/opt/yaio_home/sbin/start-yaioapp.sh"]
CMD [""]

