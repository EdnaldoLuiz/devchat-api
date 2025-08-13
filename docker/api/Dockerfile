# Etapa de build
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml ./
RUN mvn -B dependency:go-offline

COPY src src
RUN mvn -B clean package -DskipTests

# Runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar"]
