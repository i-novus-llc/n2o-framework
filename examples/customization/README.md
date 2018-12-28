Для разворачивания фронтенд-части N2O вам потребуется выполнить слеющие шаги:
> У вас должны быть установлены: NodeJs, NPM. NPM требуется настроить на локальную репозиторию i-novus.

1. создайте директорию, где будет хранится статика проекта (в этом примере - js)
2. выполните команду `npm init` в папке проекта (js)
3. выполнить команду `npm install local-n2o`
4. создайте файл index.js (он требуется для билда n2o)\
5. пример файла index.js:
```javascript
const n2o = require('local-n2o');
n2o.build(
  {
    components: {
      controls: {
        list: {
          'Super': {
            path: './src/control/superInput',
            key: 'superInput'
          }
        }
      },
      widgets: {
        list: {
          'Puper': {
            path: './src/widget/puperWidget',
            key: 'puperWidget'
          }
        }
      }
    }
  },
  {
    output: '../webapp',
    rootPath: __dirname
  }
);
```
6. запустить скрипт `node index.js`