FROM openjdk:17

EXPOSE 8080

COPY target/sandbox.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]