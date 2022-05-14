#!/bin/sh
set -e
export AWS_REPO_NAME=$1
docker build -t CCBDA-Project/frontend .
docker tag CCBDA-Project/frontend:latest $AWS_REPO_NAME/web:latest
docker push $AWS_REPO_NAME/web:latest
