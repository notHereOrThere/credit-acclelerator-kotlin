FROM maven:3.8.4-openjdk-11 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:11-jre-slim
RUN groupadd -r appuser && useradd --no-log-init -r -g appuser appuser
WORKDIR /app
COPY --from=builder /app/target/*.jar auth.jar
RUN chown -R appuser:appuser /app
USER appuser
CMD ["java", "-jar", "auth.jar"]
