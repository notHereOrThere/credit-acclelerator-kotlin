FROM maven:3.8.4-openjdk-11 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar deal.jar
CMD ["java", "-jar", "deal.jar"]


#FROM openjdk:11-jre-slim
#WORKDIR /app
#COPY ./target/deal.jar ./deal.jar
#CMD ["java", "-jar", "deal.jar"]