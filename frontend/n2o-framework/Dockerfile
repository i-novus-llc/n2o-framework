FROM nginx:alpine

LABEL maintainer="iryabov@i-novus.ru"

RUN apk add tzdata
ENV TZ=Europe/Moscow

COPY target/classes/META-INF/resources/docs/storybook /usr/share/nginx/html/storybook
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]