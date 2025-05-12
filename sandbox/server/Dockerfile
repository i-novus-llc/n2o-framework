FROM adoptopenjdk/openjdk14:alpine-slim

EXPOSE 8080

COPY target/sandbox.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]