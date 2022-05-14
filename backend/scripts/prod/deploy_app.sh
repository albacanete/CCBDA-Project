#!/bin/sh
set -e
export DOCKER_IMAGE_TAG=$1
docker-compose -f docker-compose.yml -f docker-compose.prod.yml config > production.yml
docker-compose -f production.yml pull
docker stack deploy -c production.yml api --with-registry-auth
