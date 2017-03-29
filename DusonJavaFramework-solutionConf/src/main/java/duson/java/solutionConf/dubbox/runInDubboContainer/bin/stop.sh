#!/bin/bash
PID=mqServer\.pid
kill `cat $PID` 
rm -rf $PID
echo " stop success"