#!/bin/bash
# yaio-demoapp
#
# description: start yaio demoapp
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

