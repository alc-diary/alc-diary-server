#!/usr/bin/env bash

function find_idle_profile()
{
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

  if [ ${RESPONSE_CODE} -ge 400 ]

  then
    CURRENT_PROFILE=product-2
  else
    CURRENT_PROFILE=$(curl -s http://localhost/profile)
  fi

  if [ ${CURRENT_PROFILE} == product-1 ]
  then
    IDLE_PROFILE=product-2
  else
    IDLE_PROFILE=product-1
  fi

  echo "${IDLE_PROFILE}"
}

function find_idle_port()
{
  IDLE_PROFILE=$(find_idle_profile)

  if [ ${IDLE_PROFILE} == product-1 ]
  then
    echo "8080"
  else
    echo "8081"
  fi
}
