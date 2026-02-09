FROM gradle:8.14.4-jdk21-alpine AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle bootJar -x test

FROM eclipse-temurin:21-jre-alpine-3.23 AS production
COPY --from=build /app/build/libs/*.jar app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]