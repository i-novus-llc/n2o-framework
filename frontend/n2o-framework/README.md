## Как запустить

```
$ npm install
$ npm run bootstrap
$ npm run dev
```

## Команды

### npm run bootstrap
lerna bootstrap

### yarn run build
сборка всех пакетов

### Использование компонентов N2O
Для импорта всех компонентов, необходимо использовать функцию `createFactoryConfig`.
Например:
```
import createFactoryConfig from "n2o-framework/lib/core/factory/createFactoryConfig";

<N2O {...createFactoryConfig(config)} />
```
 где config это кастомная настройка.

Для использования "легкой" настройки, необходимо использовать функцию `createFactoryConfigLight`.
Например:
```
import createFactoryConfigLight from "n2o-framework/lib/core/factory/createFactoryConfigLight";

<N2O {...createFactoryConfigLight(config)} />
```
`createFactoryConfigLight` содержит компоненты из `createFactoryConfig`, за исключением:
* CodeEditor
* TextEditor
* ChartWidget
