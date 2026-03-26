FROM maven:3.9.8-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

RUN useradd -ms /bin/sh devopsuser
USER devopsuser

COPY --from=builder /app/target/vibh-app-*.jar app.jar
cd vagr
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]