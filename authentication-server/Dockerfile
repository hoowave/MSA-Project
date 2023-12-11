FROM openjdk:19-jdk-alpine AS builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN rm src/main/resources/bootstrap.yml
RUN mv src/main/resources/bootstrap-docker.yml src/main/resources/bootstrap.yml
RUN chmod +x ./gradlew
RUN ./gradlew bootJAR

FROM openjdk:19-jdk-alpine
WORKDIR /app
COPY --from=builder build/libs/*.jar /app/app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]