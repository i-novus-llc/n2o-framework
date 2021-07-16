# Сборка и запуск

Необходимо собрать frontend статику следующими командами:

```
cd /frontend  

Если используете yarn (рекомендуется):  
- yarn
- yarn run build

Если используете npm:  
- npm install  
- npm run build  
```

Далее собираем и запускаем сервер:

```
cd ../
mvnw spring-boot:run
```

Примеры откроются по адресу http://localhost:8080