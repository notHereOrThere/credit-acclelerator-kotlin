FROM maven:3.8.4-openjdk-11 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM openjdk:11-jre-slim
RUN groupadd -r appuser && useradd --no-log-init -r -g appuser appuser
WORKDIR /app
COPY --from=builder /app/target/*.jar gateway.jar
RUN chown -R appuser:appuser /app
USER appuser
CMD ["java", "-jar", "gateway.jar"]
