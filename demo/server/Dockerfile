FROM openjdk:8-jre-alpine

LABEL maintainer="iryabov@i-novus.ru"

RUN apk add tzdata
ENV TZ=Europe/Moscow

EXPOSE 8080

ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]