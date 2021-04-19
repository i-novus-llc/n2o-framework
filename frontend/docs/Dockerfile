FROM nginx:alpine

COPY build /usr/share/nginx/html/docusaurus/
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]