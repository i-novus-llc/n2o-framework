FROM nginx:alpine

LABEL maintainer="iryabov@i-novus.ru"

RUN apk add tzdata
ENV TZ=Europe/Moscow

EXPOSE 80

COPY target/META-INF/resources /usr/share/nginx/html