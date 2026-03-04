# n2o-framework (frontend)

`n2o-framework` — React/TypeScript библиотека для построения UI на базе N2O: виджеты, страницы, инфраструктура (redux/saga), утилиты и стили.

## Быстрый старт

### Установка зависимостей
```yarn install```

Собрать библиотеку TypeScript в `lib/` и стили/шрифты в `dist/`
### Сборка
```yarn build```

---

## Основная структура src

* actions — action creators
* components — UI-часть framework: страницы, виджеты, контейнеры, общие компоненты
* constants — константы, enum'ы, ключи, типовые значения
* core — ядро: фабрики/реестр компонентов, инфраструктура, базовые механизмы N2O
* ducks — redux логика (action+reducer+sagas(компонентов)+types рядом)
* sagas — мета saga-логика (watchers/workers, эффекты)
* factoryComponents — компоненты/адаптеры, используемые и регистрируемые во factories
* impl — кнопки выполняющие конкретные действия
* plugins — плагины/расширения framework (подключаемые модули)
* tools — инструменты, вспомогательные модули
* utils — утилиты общего назначения
* locales — локализации (словари/ресурсы i18n)
* sass —  стили framework (SCSS)

---

## Использование компонентов N2O

В N2O компоненты подключаются через конфигурацию **factories** (реестр компонентов/адаптеров). Есть два основных способа: быстрый (через `createFactoryConfig`) и ручной (тонкая настройка/частичный импорт).

### 1) Быстро: импорт всех базовых factories через `createFactoryConfig`

Используйте `createFactoryConfig`, чтобы объединить стандартную конфигурацию `n2o-framework` с вашей кастомной конфигурацией.

```
import createFactoryConfig from 'n2o-framework/lib/core/factory/createFactoryConfig';

<N2O {...createFactoryConfig(config)} />
```

Где `config` — ваши переопределения/расширения (например виджеты, редьюсеры, саги, evalContext).

Пример: добавляем виджет `CustomWidget` и переопределяем стандартный `FormWidget`
(зарезервированные имена можно посмотреть в переменной `factories` внутри `createFactoryConfig`):


```
const widgets = {
    CustomWidget,
    FormWidget,
}
```


Подключение redux-частей:
- редьюсеры добавляются ключом `customReducers`
- саги — ключом `customSagas`


```
const customReducers = { yourReducer }
const customSagas = [...yourSagas]
```

Расширение контекста вычислений (evalContext): можно добавить проектные функции, доступные во встроенных JS-выражениях.
Пример ориентируйтесь на `src/utils/functions.ts`.

```
const evalContextFunctions = {
    $prefix: { yourFunc },
}

const evalContext = {  ...evalContextFunctions }
```

Итоговая сборка:

```
const config = createFactoryConfig({
      widgets,
      customReducers,
      customSagas,
      evalContext
})

export const App = () => <N2O {...config} />
```


### 2) Тонко: собрать factories вручную (без `createFactoryConfig`)

Подходит, если вы хотите:
- подключать только часть компонентов,
- контролировать lazy‑загрузку,
- минимизировать бандл.

Шаги:
1. Импортировать нужные компоненты (ориентируясь на то, что использует `createFactoryConfig`)
2. Для lazy‑загрузки использовать `defineAsync`
3. Объединить factories с вашей локальной конфигурацией (например через `deepmerge`)
4. Передать конфигурацию в `N2O`

Пример: подключаем только два виджета, `HtmlWidget` грузим lazy:

```
import { defineAsync } from 'n2o-framework/lib/core/factory/defineAsync'
import { FormWidget } from 'n2o-framework/lib/components/widgets/Form/FormWidget'

export const widgets = {
    HtmlWidget: defineAsync(() => import('n2o-framework/lib/components/widgets/Html/HtmlWidget')
        .then(({ HtmlWidget }) => HtmlWidget)),
    FormWidget,
}

export const n2oFactories = { widgets }
export const localFactories = { ...yourFactories }

export const config = deepmerge(n2oFactories, localFactories)

export const App = () => <N2O {...config} />
```


## Пакеты рядом

- `@i-novus/n2o-components` — UI-компоненты, используемые внутри `n2o-framework` (лежит в `frontend/n2o-components`).
