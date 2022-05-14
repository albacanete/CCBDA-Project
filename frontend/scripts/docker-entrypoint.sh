#!/bin/sh
set -e

echo "Environment substitution in nginx configuration"

envsubst '\${NGINX_HOST} \${API_UPSTREAM} \${BASE_HREF}' < /etc/nginx/conf.d/nginx-default.tmpl > /etc/nginx/conf.d/default.conf

exec "$@"
