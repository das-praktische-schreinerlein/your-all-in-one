#!/bin/bash
# yaio-demoapp
#
# description: start yaio demoapp
APPPATH=/var/www/vhosts/your-all-in-one.de/yaio-appdemo
cd $APPPATH
case $1 in
    start)
        /bin/bash $APPPATH/sbin/start-yaioapp.sh
    ;;
    stop)
        /bin/bash $APPPATH/sbin/stop-yaioapp.sh
    ;;
    restart)
        /bin/bash $APPPATH/sbin/stop-yaioapp.sh
        /bin/bash $APPPATH/sbin/start-yaioapp.sh
    ;;
    *)
        echo "Usage: /etc/init.d/yaio-appdemo.sh {start|stop|restart}"
        exit 1
    ;;
esac
exit 0

