#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
echo "> ABSPATH = $ABSPATH"

ABSDIR=$(dirname $ABSPATH)
echo "> ABSDIR = $ABSDIR"

source ${ABSDIR}/profile.sh

IDLE_PORT=$(find_idle_port)
echo "> IDLE_PORT = $IDLE_PORT"

IDLE_PID=$(lsof -ti tcp:${IDLE_PORT})
echo "> IDLE_PID = $IDLE_PID"

if [ -z ${IDLE_PID} ]
then
  echo "> There are no application currently running, therefor application will not exit."
else
  echo "> kill -15 $IDLE_PID"
  kill -15 ${IDLE_PID}
  sleep 5
fi
