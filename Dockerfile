FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/springboot_rest-0.0.1-SNAPSHOT.jar /app/employeeSystem.jar
ENTRYPOINT ["java", "-jar", "employeeSystem.jar"]