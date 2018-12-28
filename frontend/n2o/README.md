### Как запустить

```shell
$ yarn install                     # Установит все зависимости
```

Отредактировать tools/run.js (поставить адрес сервера, на котором запущен n2o сервер)

```js
require('http-proxy-middleware')(['/page'], {
	target: 'http://localhost:8080',
	changeOrigin: true,
}),
require('http-proxy-middleware')(['/data'], {
	target: 'http://localhost:8080',
	changeOrigin: true,
}),
```
```shell
$ yarn start                       # Запустит приложение N2O
```

### Команды
`yarn start` - запуск в dev режиме
`yarn run build` - сборка проекта в папку dist

### Инициализация
Главным файлов является `main.jsx`. В нем создается роутинг и хранилище Redux. 
Настройка роутинга находится в файле `routes.json`.  Запрос за метаданными страницы, при переходе по ссылке, проиходит автоматически в роутере. Метаданные передаются в `Page.jsx` - базовый компонент root страницы.
Компонент `Page.jsx` оборачивается компонентом `Application.jsx`, который является самым верхним в цепочке компонентов n2o. В `Page.jsx` встраивается wrapper `Layout.jsx`. В этотм момент приложение уже строится по метаданным страницы.

### Схема компонентов
[![](http://n2o.i-novus.ru/react/docs/mockups/schema.png)](http://n2o.i-novus.ru/react/docs/mockups/schema.png)

### Жизненный цикл
[![](http://n2o.i-novus.ru/react/docs/mockups/life_circle.png)](http://n2o.i-novus.ru/react/docs/mockups/life_circle.png)

### Wrapper
Для каждого динамического типа компонентов существует специальный враппер, который умеет подключать нужную реализацию исходя из метаданных.
Список врапперов:  Layout.jsx, Region.jsx, Widget.jsx, Control.jsx, Handler.jsx