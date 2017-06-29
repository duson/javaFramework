#!/bin/bash

APP_NAME=appName

bin_abs_path=$(readlink -f $(dirname $0))
base=${bin_abs_path}/..
pidfile=$base/pid.pid

if [ -f $pidfile ] ; then
	echo "found pid , Please run stop.sh first ,then start.sh" 2>&2
    exit 1
fi

JAVA_OPTS="-Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8"
APP_OPTS="-DappName=$APP_NAME -Dlog4j.configuration=conf/log4j.properties"

for i in $base/lib/*;
do CLASSPATH=$i:"$CLASSPATH";
done
CLASSPATH="$base/conf:$CLASSPATH";

current_path=`pwd`
cd $base
nohup java $JAVA_OPTS $APP_OPTS -classpath $base/conf/:$base/lib/*:. com.protruly.pay.job.SpringMain 1>$base/logs/sys.log 2>&1 &
echo $! > $pidfile
cd $current_path

echo " start success"