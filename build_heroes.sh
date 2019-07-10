#!/bin/sh
echo "Build heroes image"
cd "$1/heroes"
mvn clean install -DskipTests=true
docker build . -t heroes
