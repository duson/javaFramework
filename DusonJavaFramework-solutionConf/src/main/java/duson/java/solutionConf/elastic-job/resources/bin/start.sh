#!/bin/bash
java -classpath conf/:lib/*:. -Ddefault.client.encoding=UTF-8 -Dfile.encoding=UTF-8 -Dlog4j.configuration=conf/log4j.properties xxx.SpringMain