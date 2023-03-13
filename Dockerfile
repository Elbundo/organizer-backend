FROM openjdk:17-alpine3.14
EXPOSE 8080
ADD target/Organizer-backend-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "Organizer-backend-0.0.1-SNAPSHOT.jar"]