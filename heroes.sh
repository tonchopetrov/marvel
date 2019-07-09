docker rm -f heroes
docker run -d --name heroes --link mongo --link rabbitmq -e MONGO_HOST=mongo -e RABBIT_HOST=rabbitmq -p 8081:8081  --net marvel_default heroes



