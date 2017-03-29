#!/bin/bash
PID=mqServer\.pid
nohup java -Xms256m -Xmx512m -classpath conf/:lib/*:. -Ddefault.client.encoding=UTF-8 -Dfile.encoding=UTF-8 -Dlog4j.configuration=conf/log4j.properties com.alibaba.dubbo.container.Main &
echo $! > $PID
echo " start success"