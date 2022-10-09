#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/app
PROJECT_NAME=alc-diary

echo "> 새 애플리케이션 배포"
JAR_NAME=alc-diary-0.0.1-SNAPSHOT.jar

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행 권한 추가"
chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"
IDLE_PROFILE=$(find_idle_profile)

echo "> $JAR_NAME 를 profile=$IDLE_PROFILE 로 실행"
nohup java -jar \
  -Dspring.config.location=classpath:/application-$IDLE_PROFILE.yml \
  -Dspring.profiles.active=$IDLE_PROFILE \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
