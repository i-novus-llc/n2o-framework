FROM nginx:alpine

LABEL maintainer="iryabov@i-novus.ru"

RUN apk add tzdata
ENV TZ=Europe/Moscow

COPY target/META-INF/resources /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]