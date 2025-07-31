FROM openjdk:21

EXPOSE 8080

COPY target/sandbox.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]