FROM amazoncorretto:21 AS build

EXPOSE 8080

COPY ./build/libs/travel-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]