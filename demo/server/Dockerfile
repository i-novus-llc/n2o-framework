FROM adoptopenjdk/openjdk14:alpine-slim

EXPOSE 8080

COPY target/demo.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]