#!/bin/bash
APP_NAME=eshop-pay

bin_abs_path=$(readlink -f $(dirname $0))
base=${bin_abs_path}
pidfile=$base/pay.pid

JAVA_OPTS="-Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8"
APP_OPTS="-DappName=$APP_NAME"

current_path=`pwd`
cd $base

nohup /usr/java/jre1.8.0_144/bin/java $JAVA_OPTS $APP_OPTS -jar eshop-pay.jar 1>pay.log 2>&1 &

echo $! > $pidfile
cd $current_path

echo " start success"