#!/bin/sh
set -e

echo Django migrate
pipenv run python manage.py migrate --noinput

echo Django Collectstatic
pipenv run python manage.py collectstatic --noinput --clear
