FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /build

ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

COPY pom.xml /build/
RUN mvn dependency:resolve dependency:resolve-plugins -B

COPY ./src /build/src/
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar"]