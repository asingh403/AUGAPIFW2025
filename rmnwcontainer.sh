#!/bin/bash
# optional: remove old container only after verification
# docker rm sonarqube

docker run -d --name sonarqube \
  -p 9000:9000 \
  -v sonarqube_data:/opt/sonarqube/data \
  -v sonarqube_extensions:/opt/sonarqube/extensions \
  sonarqube_snapshot:latest

docker ps -a | grep sonarqube
docker volume ls | grep sonarqube
# open http://localhost:9000 and confirm projects and your token (or re-generate if needed)
