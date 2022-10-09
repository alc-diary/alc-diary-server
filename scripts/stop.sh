#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

IDLE_PROT=$(find_idle_port)

echo "> Application port = $IDLE_PORT"

IDLE_PID=$(lsof -ti tcp:$IDLE_PORT)

echo "> Application pid = $IDLE_PID"

if [ -z ${IDLE_PID} ]
then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $IDLE_PID"
  kill -15 ${IDLE_PID}
  sleep 5
fi
