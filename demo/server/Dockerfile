FROM openjdk:21-ea-jdk-oracle

EXPOSE 8080

COPY target/demo.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]