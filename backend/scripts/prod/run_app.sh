#!/bin/sh
set -e

echo Django migrate
#python3 manage.py migrate --noinput

echo Django runserver
python3 manage.py runserver
