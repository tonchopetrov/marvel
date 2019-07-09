docker rm -f translation
docker run -d --name translation -e RABBIT_HOST=rabbitmq -p 8082:8082  --net marvel_default translation
