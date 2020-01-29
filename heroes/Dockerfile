FROM java:8
MAINTAINER petrovtoncho@gmail.com
ENV RABBITMQ_HOSTNAME localhost
ENV MONGO_HOSTNAME localhost
ENV JAVA_DEBUG_PORT 5005
WORKDIR /
COPY target/heroes-1.0.0-SNAPSHOT.jar /home/application/heroes-1.0.0-SNAPSHOT.jar
EXPOSE 8081
CMD ["java","-DXdebug -Xrunjdwp:transport=dt_socket,server=127.0.0.1,suspend=n,address=${JAVA_DEBUG_PORT}","-jar","-DMONGO_HOST=${MONGO_HOSTNAME:localhost}","-DRABBIT_HOST=${RABBITMQ_HOSTNAME:localhost}","/home/application/heroes-1.0.0-SNAPSHOT.jar"]