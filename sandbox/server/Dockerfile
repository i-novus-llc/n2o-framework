FROM openjdk:17.0.2-jdk-oracle

EXPOSE 8080

COPY target/sandbox.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]