#!/bin/sh
echo "Build translation image"
cd "$1/translation"
mvn clean install -DskipTests=true
docker build . -t translation