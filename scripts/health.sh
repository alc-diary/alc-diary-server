#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh
source ${ABSDIR}/switch.sh

IDLE_PORT=$(find_idle_port)

echo "> Health Check Start!"
echo "> IDLE_PORT: $IDLE_PORT"
echo "> curl -s http://localhost:$IDLE_PORT/profile"
sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -s http://localhost:${IDLE_PORT}/profile)
  UP_COUNT=$(echo ${RESPONSE} | grep 'product' | wc -l)

  if [ ${UP_COUNT} -ge 1 ]
  then
    echo "> Health check success."
    switch_proxy
    break
  else
    echo "> The response of Health check is unknown or not in the running state."
    echo "> Health check: ${RESPONSE}"
  fi

  if [ ${RETRY_COUNT} -eq 10 ]
  then
    echo "> Health check fail."
    echo "> Terminates deployment without connecting to nginx"
    exit 1
  fi

  echo "> Connection to Health check fail. retry..."
  sleep 10
done
