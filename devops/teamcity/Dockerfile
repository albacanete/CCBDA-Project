FROM jetbrains/teamcity-minimal-agent:latest

LABEL dockerImage.teamcity.version="latest" \
    dockerImage.teamcity.buildNumber="latest"

# install docker
USER root
RUN apt-get update -y \
    && apt-get install -y ca-certificates curl gnupg lsb-release \
    && mkdir -p /etc/apt/keyrings \
    && curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg \
    && echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null \
    && apt-get update -y \
    && apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# configure permissions
RUN usermod -aG docker buildagent

COPY ./run-docker.sh /services/run-docker.sh
RUN chown -R buildagent:buildagent /services/ \
    & sed -i -e 's/\r$//' /services/run-docker.sh
