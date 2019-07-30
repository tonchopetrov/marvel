FROM java:8
MAINTAINER petrovtoncho@gmail.com
ENV RABBITMQ_HOSTNAME localhost
WORKDIR /
COPY target/translation-1.0.0-SNAPSHOT.jar /home/application/translation-1.0.0-SNAPSHOT.jar
EXPOSE 8082
CMD ["java","-jar","-DRABBIT_HOST=${RABBITMQ_HOSTNAME}","/home/application/translation-1.0.0-SNAPSHOT.jar"]