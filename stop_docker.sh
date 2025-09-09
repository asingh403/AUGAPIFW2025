#!/bin/bash
docker stop sonarqube
mkdir -p ~/sonar_backup/{data,extensions,conf,logs}
docker cp sonarqube:/opt/sonarqube/data ~/sonar_backup/data
docker cp sonarqube:/opt/sonarqube/extensions ~/sonar_backup/extensions
docker cp sonarqube:/opt/sonarqube/conf ~/sonar_backup/conf
docker cp sonarqube:/opt/sonarqube/logs ~/sonar_backup/logs
docker run --rm -v sonarqube_data:/target -v ~/sonar_backup/data:/src alpine sh -c "cp -a /src/. /target/"
docker run --rm -v sonarqube_extensions:/target -v ~/sonar_backup/extensions:/src alpine sh -c "cp -a /src/. /target/"
