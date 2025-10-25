FROM amazoncorretto:21 AS build

LABEL maintainer="jiyeon2499@gmail.com"

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=build/libs/travel-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} my-travel-destination.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/my-travel-destination.jar"]