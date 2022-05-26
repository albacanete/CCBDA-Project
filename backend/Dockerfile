FROM python:3.10

# Define environment variables
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1
ENV PIP_NO_CACHE_DIR false
ENV PIPENV_VENV_IN_PROJECT true

WORKDIR /app

# [Docker-ref] requirements.txt
# COPY Pipfile Pipfile.lock ./

# Install Dependencies
RUN apt-get update -y && apt-get install -y \
    libpq-dev \
    gcc \
  && rm -rf /var/lib/apt/lists/* \
  && pip install --upgrade pip \
  && pip install pipenv wait-for-it \
  && pipenv lock \
  && pipenv sync \
  && apt-get purge -y --auto-remove gcc

# COPY . .
COPY ./ccbda_project /app/ccbda_project
COPY ./djangoProject3 /app/djangoProject3
COPY ./manage.py /app/manage.py
COPY ./requirements.txt /app/requirements.txt
RUN pip install -r requirements.txt
COPY ./scripts /app/scripts
RUN chmod +x -R /app/scripts/
COPY ./templates /app/templates

# ENTRYPOINT ["tail", "-f", "/dev/null"]

EXPOSE 8000
CMD ["/app/scripts/prod/run_app.sh"]
