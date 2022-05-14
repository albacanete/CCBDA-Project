FROM python:3.10-slim

# Define environment variables
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1
ENV PIP_NO_CACHE_DIR false
ENV PIPENV_VENV_IN_PROJECT true

WORKDIR /app

# [Docker-ref] requirements.txt
COPY Pipfile Pipfile.lock ./

# Install Dependencies
RUN apt-get update -y && apt-get install -y \
    libpq-dev \
    gcc \
  && rm -rf /var/lib/apt/lists/* \
  && pip install --upgrade pip \
  && pip install pipenv wait-for-it \
  && pipenv sync \
  && apt-get purge -y --auto-remove gcc

COPY . .
ENTRYPOINT ["/app/scripts/docker-entrypoint.sh"]

EXPOSE 8000
CMD ["/app/scripts/prod/run_app.sh"]
