FROM maven:3.8.4-openjdk-11 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar dossier.jar
CMD ["java", "-jar", "dossier.jar"]

#FROM openjdk:11-jre-slim
#WORKDIR /app
#COPY ./target/dossier-1.0.0.jar ./dossier.jar
#CMD ["java", "-jar", "dossier.jar"]