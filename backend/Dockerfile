FROM gradle:8.14.4-jdk21-alpine AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle bootJar -x test

FROM eclipse-temurin:21-jre-alpine-3.23 AS production
RUN addgroup -S spring && adduser -S spring -G spring
WORKDIR /app
COPY --from=build --chown=spring:spring /app/build/libs/*.jar app.jar
USER spring
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]