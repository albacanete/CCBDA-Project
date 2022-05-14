#!/bin/sh
set -e

echo "Waiting for database..."

wait-for-it -s "$DJANGO_DB_HOST":"$DJANGO_DB_PORT" -- echo "Database started"

exec "$@"
