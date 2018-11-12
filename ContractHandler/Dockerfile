FROM maven:3.5.4-jdk-8-alpine as build
WORKDIR /app
COPY pom.xml ./pom.xml
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package

FROM openjdk:8-jre-alpine
COPY --from=build /app/target/contract-handler-0.1.jar /app/app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/app/app.jar"]
