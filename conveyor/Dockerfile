FROM maven:3.8.4-openjdk-11 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar conveyor.jar
CMD ["java", "-jar", "conveyor.jar"]
