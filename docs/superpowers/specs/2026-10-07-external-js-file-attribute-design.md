# Вынос тела элемента во внешний файл (атрибут `file`)

Дата: 2026-10-07

## Цель

Разработчики пишут длинные JS-выражения в теле тегов и повторяют их в нескольких местах.
Нужна возможность вынести тело тега в файл в ресурсах проекта и сослаться на него атрибутом.

Сейчас такая возможность есть только у двух тегов, и атрибуты называются по-разному:
`<html url>` и `<condition src>`. Цель — единое имя атрибута для всех тегов, тело которых
может содержать JS-выражение.

## Решения

| Вопрос | Решение |
|---|---|
| Имя атрибута | `file` |
| Объём | все теги с JS-выражением в теле (см. «Затрагиваемые теги») |
| `<condition src>`, `<html url>` | остаются как устаревшие синонимы + правило в n2o-migrate |
| Формат файла | 1 файл = 1 выражение; содержимое файла идентично тому, что писалось бы в теле |
| Где подставляется содержимое | в компиляторах через общий метод `CompileProcessor.resolveText` |

### Почему `file`

- Атрибут уже существует с этим смыслом: `<sql file>`, `<test file>` — «тело берётся из файла».
- `src` во фреймворке означает React-компонент (действия, ячейки, поля, филдсеты, меню);
  у действия `<set-value>` атрибут `src` уже занят.
- `url` означает маршрут/адрес запроса и нормализуется как роут.
- Имя не привязано к JS, поэтому одинаково подходит для JS, HTML, SQL, JSON.

### Отклонённые подходы к подстановке

- **В IO при чтении** — у IO нет доступа к `ExternalFilesLoader`; при persist (в т.ч. n2o-migrate)
  ссылка на файл заменилась бы содержимым.
- **SourceTransformer** — трансформеры работают на уровне корневых метаданных; для вложенных
  зависимостей нужен рекурсивный обход дерева исходников.

## Затрагиваемые теги

| # | Тег | Source-класс | Поле тела | Поле файла |
|---|---|---|---|---|
| 1–7 | зависимости поля: `<enabling>`, `<visibility>`, `<requiring>`, `<set-value>`, `<reset>`, `<fetch>`, `<fetch-value>` | `N2oField.Dependency` | `value` | `file` |
| 8 | зависимости виджета: `<visibility>`, `<enabling>` | `N2oDependency` | `value` | `file` |
| 9 | зависимости кнопок: `<visibility>`, `<enabling>` | `N2oAbstractButton.Dependency` | `value` | `file` |
| 10 | `<visibility>` колонки таблицы | `N2oBaseColumn.ColumnVisibility` | `value` | `file` |
| 11 | действие `<set-value>` | `N2oSetValueAction` | `expression` | `file` |
| 12 | валидация `<condition>` (в объекте и в поле) | `N2oConditionValidation` | `expression` | `file` (заменяет `src`) |
| 13 | `<fetch-value>` в `<inherited-datasource>` | `N2oInheritedDatasource` | `fetchValue` | `fetchValueFile` |
| 14 | `<submit-value>` в `<submit>` | `N2oInheritedDatasource.Submit` | `submitValue` | `submitValueFile` |
| — | виджет `<html>` (не JS, но тот же механизм) | `N2oHtmlWidget` | `html` | `file` (заменяет `url`) |

Не затрагиваются (тело не является JS): `<sql>`, `<graphql>`, `<rest>` в провайдерах, тело
`<filter>` в query, `<text>`, `<code-viewer>`, `<markdown>`, поле `<html>`, кейсы ячейки `<switch>`.

## Модель (n2o-api)

Новый интерфейс:

```java
/** Тело элемента может быть вынесено во внешний файл */
public interface N2oFileAware {
    String getFile();
    void setFile(String file);
}
```

Его реализуют классы строк 1–12 и `N2oHtmlWidget`. Поле `file` добавляется в базовые классы
зависимостей, чтобы все подтипы получили его автоматически.

`N2oInheritedDatasource` и `N2oInheritedDatasource.Submit` интерфейс не реализуют: у них тело
находится в дочернем элементе, поэтому добавляются отдельные поля `fetchValueFile` и
`submitValueFile`.

Существующие `N2oConditionValidation.src` и `N2oHtmlWidget.url` удаляются из модели; IO для
устаревших атрибутов пишет значение в `file`.

## XSD

В каждый затрагиваемый элемент добавляется:

```xml
<xs:attribute name="file" type="xs:string" use="optional">
    <xs:annotation>
        <xs:documentation>
            Путь к файлу в ресурсах проекта, содержимое которого используется вместо тела элемента
        </xs:documentation>
    </xs:annotation>
</xs:attribute>
```

Для `<fetch-value>` и `<submit-value>` атрибут добавляется в сами эти элементы.

Схемы: `control-3.0`, `widget-5.0`, `button-2.0` / `base/toolbar-2.0`, `action-2.0`,
`object-4.0`, `datasource-1.0`.

В документации `<condition src>` и `<html url>` добавляется: «Устарело, используйте атрибут file».

## IO (n2o-config)

- Теги с `p.text(...)`: добавляется `p.attribute(e, "file", t::getFile, t::setFile)`.
- `<fetch-value>`, `<submit-value>`: сразу после `childrenText` вызывается `childAttribute`.
  Persister `childAttribute` переиспользует уже созданный `childrenText` элемент, поэтому дубля не будет:

  ```java
  p.childrenText(e, "fetch-value", ds::getFetchValue, ds::setFetchValue);
  p.childAttribute(e, "fetch-value", "file", ds::getFetchValueFile, ds::setFetchValueFile);
  ```

- `<condition>`: читаются `file` и устаревший `src` в одно поле `file`.
  `<html>`: читаются `file` и устаревший `url` в одно поле `file`.
  Если задан устаревший атрибут, пишется warning: «Атрибут 'src' элемента `<condition>` устарел, используйте 'file'».
  Persist всегда записывает `file`.

## Компиляция

В `CompileProcessor` добавляется метод (реализация в `N2oCompileProcessor`):

```java
/** Тело элемента или, если оно не задано, содержимое внешнего файла */
String resolveText(String body, String file);
// castDefault(body, () -> getExternalFile(file))
```

В компиляторах прямое чтение тела заменяется на `p.resolveText(...)`:

| Компилятор | Что заменяется |
|---|---|
| `FieldCompiler` | значение зависимостей поля |
| `BaseWidgetCompiler` | значение зависимостей виджета |
| `BaseButtonCompiler` | значение зависимостей кнопки |
| `SimpleColumnCompiler` | значение видимости колонки |
| `SetValueActionCompiler` | `expression` |
| `InheritedDatasourceCompiler` | `fetchValue` / `fetchValueFile`, `submitValue` / `submitValueFile` |
| `ConditionValidationCompiler` | `getSrc()` → `getFile()` |
| `HtmlWidgetCompiler` | `getUrl()` → `getFile()` |

В `HtmlWidgetCompiler` вызов `RouteUtil.normalizeUrl(...)` сохраняется и применяется к полю `file`.
Нормализация срабатывает только в старом режиме роутинга и только приводит путь к виду роута
(например, добавляет ведущий `/`). `DefaultResourceLoader` находит ресурс в classpath в обоих
вариантах, поэтому поведение `<html url>` не меняется, а `<html file>` работает так же.

Дальнейшая обработка не меняется (`ScriptProcessor.resolveFunction` и т.д.).
Клиентская часть и формат JSON страницы не меняются.

Путь разрешается через `ExternalFilesLoader`. В `N2oExternalFilesLoader` используется
`DefaultResourceLoader`, поэтому путь без префикса ищется в classpath:
`file="scripts/fullName.js"` → `src/main/resources/scripts/fullName.js`.
`SandboxExternalFilesLoader` поддерживает файлы проекта sandbox.

## Валидация

В существующих валидаторах (поля, виджета, кнопки, колонки, действия, datasource, объекта):

1. Если одновременно заданы тело и `file`, бросается `N2oMetadataValidationException`. Пример сообщения:
   «Для зависимости `<set-value>` поля 'name' одновременно задано тело и атрибут 'file'».
   Для `<fetch-value>` и `<submit-value>` проверяются пары `fetchValue`/`fetchValueFile` и
   `submitValue`/`submitValueFile`.
2. Если не задано ни тело, ни `file`, ошибка выдаётся только там, где уже есть проверка
   на пустое тело. Новые проверки обязательности не добавляются.
3. Существование файла валидатор не проверяет: если файла нет, `ExternalFilesLoader` бросает
   исключение «Файл '...' не найден» при компиляции.

## Миграция (n2o-migrate)

- `<condition src="...">` переименовывается в `file`.
- `<html url="...">` переименовывается в `file`, только если значение является путём к ресурсу.
  Значения `http(s)://...` не трогаются и попадают в отчёт миграции.

Удаление `src`/`url` из XSD выносится в отдельную задачу на следующую мажорную версию схем.

## Тестирование

Только юнит-тесты в n2o-config: клиентская часть не меняется, поэтому автотесты не нужны.

- **IO.** По одному тесту на чтение и запись для каждой группы тегов. `file` должен сохраняться при persist;
  `<fetch-value file>` и `<submit-value file>` не должны дублировать элементы.
- **Компиляция.** На каждый компилятор из таблицы тест с `file`: скомпилированное выражение совпадает
  с вариантом, где выражение задано в теле. Тестовые `.js` лежат в `src/test/resources/.../file/`.
- **Валидация.** Если заданы и тело, и `file`, бросается исключение. Устаревшие `src`/`url` продолжают работать.
- **Миграция.** Проверяются `src` → `file`, `url` → `file`, и что `http(s)` URL остаются нетронутыми.

## Документация и sandbox

- В XSD у всех тегов одинаковое описание атрибута `file`.
- Раздел документации «Вынос выражений в файл»: список поддерживающих тегов, пометка об устаревших `src`/`url`.
- Пример в sandbox: две зависимости `<set-value>` ссылаются на один `.js` файл.
