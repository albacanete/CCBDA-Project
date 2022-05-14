# Install stage
FROM node:18-alpine as install
WORKDIR /app
COPY package.json package-lock.json /app/
RUN npm ci
COPY . /app/

# Builder stage
FROM install as builder
ARG env=production
ARG base_href=/
RUN npm run build:$env -- --base-href=$base_href --deploy-url=$base_href

# Web stage
FROM nginx:stable-alpine as web

ENV BASE_HREF=$base_href
ENV NGINX_HOST=localhost
ENV API_UPSTREAM=api:8000

COPY --from=builder /app/scripts/docker-entrypoint.sh /app/scripts/docker-entrypoint.sh
COPY --from=builder /app/dist /usr/share/nginx/html
COPY ./scripts/config/nginx-default.conf /etc/nginx/conf.d/nginx-default.tmpl

ENTRYPOINT ["/app/scripts/docker-entrypoint.sh"]
EXPOSE 80 443
CMD ["nginx", "-g", "daemon off;"]
