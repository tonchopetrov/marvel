FROM java:8
MAINTAINER petrovtoncho@gmail.com
WORKDIR /
COPY target/heroes-1.0.0-SNAPSHOT.jar /home/application/heroes-1.0.0-SNAPSHOT.jar
EXPOSE 8081
CMD ["java","-jar","/home/application/heroes-1.0.0-SNAPSHOT.jar"]
