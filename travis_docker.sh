#!/bin/bash

set -ev

if [ "${TRAVIS_PULL_REQUEST}" = "false" ]; then
  if [ "${TRAVIS_BRANCH}" = "master" ]; then
    DOCKER_TAG=latest
  else
    DOCKER_TAG=$TRAVIS_BRANCH
  fi

  docker login -e="$DOCKER_EMAIL" -u="$DOCKER_USER" -p="$DOCKER_PASSWD"
  docker build -t jcooky/jandy:$DOCKER_TAG .
  docker push jcooky/jandy:$DOCKER_TAG
fi

