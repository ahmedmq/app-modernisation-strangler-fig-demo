FROM gradle:7.6.1-jdk17-jammy AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test

FROM eclipse-temurin:17-jdk-focal
EXPOSE 9090
COPY --from=build /home/gradle/src/build/libs/petclinic-owner-service-0.0.1-SNAPSHOT.jar /app/
RUN bash -c 'touch /app/petclinic-owner-service-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-jar","/app/petclinic-owner-service-0.0.1-SNAPSHOT.jar"]