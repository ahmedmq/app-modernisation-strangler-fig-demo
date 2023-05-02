FROM maven:3.8.4-openjdk-8-slim AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests

FROM tomcat:8.5-jdk8-openjdk-slim

COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/
