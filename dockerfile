FROM openjdk:17.0.5

WORKDIR /app

COPY server.jar /app

CMD ["java", "-jar", "server.jar"]