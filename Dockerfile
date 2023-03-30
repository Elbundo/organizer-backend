FROM ghcr.io/graalvm/graalvm-ce:22.3.1
EXPOSE 8080
ADD target/Organizer-backend-1.0.2.jar .
ENTRYPOINT ["java", "-jar", "Organizer-backend-1.0.2.jar"]