#!/bin/sh
set -e
export AWS_REPO_NAME=$1
docker build -t CCBDA-Project/backend .
docker tag CCBDA-Project/backend:latest $AWS_REPO_NAME/backend:latest
docker push $AWS_REPO_NAME/backend:latest
