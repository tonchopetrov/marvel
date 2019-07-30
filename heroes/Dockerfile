FROM java:8
MAINTAINER petrovtoncho@gmail.com
ENV RABBITMQ_HOSTNAME localhost
ENV MONGO_HOSTNAME localhost
WORKDIR /
COPY target/heroes-1.0.0-SNAPSHOT.jar /home/application/heroes-1.0.0-SNAPSHOT.jar
EXPOSE 8081
CMD ["java","-jar","-DMONGO_HOST=${MONGO_HOSTNAME}","-DRABBIT_HOST=${RABBITMQ_HOSTNAME}","/home/application/heroes-1.0.0-SNAPSHOT.jar"]