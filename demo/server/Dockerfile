FROM adoptopenjdk/openjdk14:alpine-slim

LABEL maintainer="iryabov@i-novus.ru"

RUN apk add tzdata
ENV TZ=Europe/Moscow

EXPOSE 8080

COPY target/demo.jar demo.jar
ENTRYPOINT ["java","-jar","demo.jar"]