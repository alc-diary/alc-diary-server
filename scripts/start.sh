#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/app
PROJECT_NAME=alc-diary

echo "> Build file copy"
echo "> cp $REPOSITORY/test/*.jar $REPOSITORY/"
cp $REPOSITORY/test/*.jar $REPOSITORY/

echo "> new application deploy"
JAR_NAME=$REPOSITORY/alc-diary-0.0.1-SNAPSHOT.jar

echo "> JAR Name: $JAR_NAME"

echo "> Grant +x to $JAR_NAME "
chmod +x $JAR_NAME

echo "> execute $JAR_NAME"
IDLE_PROFILE=$(find_idle_profile)

echo "> Execute $JAR_NAME profile=$IDLE_PROFILE"
nohup java -jar \
  -Dspring.config.location=classpath:/application-$IDLE_PROFILE.yml \
  -Dspring.profiles.active=$IDLE_PROFILE \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
