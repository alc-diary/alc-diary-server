#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
  IDLE_PORT=$(find_idle_port)

  echo "> Change port to $IDLE_PORT"
  echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" |
  sudo tee /etc/nginx/default.d/service-url.inc

  echo "> Nginx Reload"
  sudo service nginx reload
}
