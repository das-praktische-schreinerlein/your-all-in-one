#!/bin/bash
### BEGIN INIT INFO
# Provides:          yaio-demoapp
# Required-Start:    $network $remote_fs $syslog
# Required-Stop:     $network $remote_fs $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start yaio-demoapp at boot time
# Description:       Start yaio-demoapp at boot time
### END INIT INFO
APPPATH=/var/www/vhosts/your-all-in-one.de/yaio-appdemo
YAIOUSER=yaiodemo
cd $APPPATH
case $1 in
    start)
        su --preserve-environment --shell /bin/bash -c $APPPATH/sbin/start-yaioapp.sh $YAIOUSER
    ;;
    stop)
        su --preserve-environment --shell /bin/bash -c  $APPPATH/sbin/stop-yaioapp.sh $YAIOUSER
    ;;
    restart)
        su --preserve-environment --shell /bin/bash -c $APPPATH/sbin/stop-yaioapp.sh $YAIOUSER
        su --preserve-environment --shell /bin/bash -c $APPPATH/sbin/start-yaioapp.sh $YAIOUSER
    ;;
    *)
        echo "Usage: /etc/init.d/yaio-appdemo.sh {start|stop|restart}"
        exit 1
    ;;
esac
exit 0

