# Fields / Input Controls (control-3.0)

Schema: `http://n2oapp.net/framework/config/schema/control-3.0`
Fields are placed inside `<fields>` of a form widget, or inside `<filters>` of a table.

## Base Field Attributes (inherited by all controls)
| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Field identifier (required) | |
| label | String | Field label | From query field name |
| label-class | String | CSS class for field label | |
| help | String | Help tooltip text | |
| help-trigger | hover/click | Help tooltip trigger | click |
| placeholder | String | Input placeholder | |
| required | boolean | Required field | false |
| visible | boolean / String | Visibility (can be JS expression) | true |
| enabled | boolean / String | Enabled (can be JS expression) | true |
| no-label | boolean / String | Hide label text (supports JS) | false |
| no-label-block | boolean / String | Hide label block entirely (supports JS) | false |
| description | String | Description under field | |
| default-value | String | Default value | |
| param | String | URL parameter for default value | |
| ref-model | resolve/edit/filter/selected/datasource | Model for value initialization | resolve |
| ref-datasource | String | Datasource for value initialization | current widget |
| ref-field-id | String | Field for value initialization | |
| copied | boolean | Copy on merge defaults | true |
| src | String | Custom React component | |
| class | String | CSS class for field component | |
| style | String | CSS style for field component | |

---

## Simple Input Controls

### `<input-text>` — Text/Number Input
| Attribute | Type | Default |
|---|---|---|
| domain | String | |
| length | Number | |
| precision | Number | |
| max / min | Number | |
| step | Number | |
| measure | String | |
| autocomplete | String | off |
```xml
<input-text id="name" label="Name" required="true" length="100"/>
<input-text id="age" label="Age" domain="integer" min="0" max="150"/>
<input-text id="salary" label="Salary" domain="numeric" precision="2" measure="₽"/>
```

### `<text-area>` — Multi-line Text
| Attribute | Type | Default |
|---|---|---|
| min-rows | Number | 3 |
| max-rows | Number | 3 |
| max-length | Number | |

### `<password>` — Password Input
| Attribute | Type | Default |
|---|---|---|
| eye | boolean | true |
| length | Number | |
| autocomplete | String | off |

### `<date-time>` — Date/Time Picker
| Attribute | Type | Default |
|---|---|---|
| date-format | DD.MM.YYYY / DD/MM/YYYY / etc. | DD.MM.YYYY |
| time-format | HH:mm / HH:mm:ss | |
| min / max | String (ISO 8601) | |
| utc | boolean | false |
| autocomplete | String | off |
```xml
<date-time id="birthday" label="Birthday" date-format="DD.MM.YYYY"/>
<date-time id="createdAt" label="Created" date-format="DD.MM.YYYY" time-format="HH:mm"/>
```

### `<date-interval>` — Date Range
| Attribute | Type | Default |
|---|---|---|
| date-format | DD.MM.YYYY / DD/MM/YYYY | |
| time-format | HH:mm / HH:mm:ss | |
| min / max | String (ISO 8601) | |
| utc | boolean | false |
| begin-param | String | |
| end-param | String | |
```xml
<date-interval id="period" label="Period" date-format="DD.MM.YYYY">
    <default-value begin="2024-01-01" end="2024-12-31"/>
</date-interval>
```

### `<checkbox>` — Checkbox
| Attribute | Type | Default |
|---|---|---|
| unchecked | null / false | null |

### `<masked-input>` — Input with Mask
| Attribute | Type | Default |
|---|---|---|
| mask | String (e.g. `+7 (999) 999-99-99`) | |
| measure | String | |
| clear-on-blur | boolean | true |
| autocomplete | String | off |
| invalid-text | String | Data does not match the field format |

### `<email>` — Email Input (preset mask)
### `<phone>` — Phone Input (`country="RU, KZ"`)
### `<snils>` — SNILS Input (Russian social number)

### `<input-money>` — Money Input
| Attribute | Type | Default |
|---|---|---|
| prefix | String | |
| suffix | String | |
| thousands-separator | String | |
| decimal-separator | String | |
| integer-limit | Number | |
| fraction-formatting | off / manual / auto | off |
| autocomplete | String | off |

### `<slider>` — Range Slider
| Attribute | Type | Default |
|---|---|---|
| mode | single / range | single |
| min / max | Number | |
| step | Number | 1 |
| measure | String | |
| vertical | boolean | false |

### `<number-picker>` — +/- Number Selector
| Attribute | Type | Default |
|---|---|---|
| min | Number | 0 |
| max | Number | 100 |
| step | Number | 1 |

### `<rating>` — Star Rating
| Attribute | Type | Default |
|---|---|---|
| max | Number | 5 |
| half | boolean | false |
| show-tooltip | boolean | |

### `<time-picker>` — Time Selector
| Attribute | Type | Default |
|---|---|---|
| prefix | String | |
| mode | hour,minute,second / hour,minute / hour / minute | hour,minute,second |
| time-format | String | HH:mm:ss |
| format | symbols / digit | symbols |

### `<code-editor>` — Code Editor
| Attribute | Type | Default |
|---|---|---|
| language | sql / xml / html / javascript / groovy / java | |
| min-lines | Number | 5 |
| max-lines | Number | |

### `<text-editor>` — Rich Text Editor (WYSIWYG)
| Attribute | Type | Default |
|---|---|---|
| toolbar-url | String | |

### `<progress>` — Progress Bar
| Attribute | Type | Default |
|---|---|---|
| max | Number | |
| bar-text | String | |
| animated | boolean | false |
| striped | boolean | false |
| color | String | |
| bar-class | String | |

### `<status>` — Status Indicator
| Attribute | Type | Default |
|---|---|---|
| color | String | |
| text | String (required) | |
| text-position | left / right | right |

### `<file-upload>` — File Upload
| Attribute | Type | Default |
|---|---|---|
| multi | boolean | false |
| ajax | boolean | true |
| upload-url | String | |
| delete-url | String | |
| value-field-id | String | |
| label-field-id | String | |
| url-field-id | String | |
| message-field-id | String | message |
| request-param | String | |
| accept | String | |
| show-size | boolean | true |
| show-name | boolean | false |

### `<image-upload>` — Image Upload
| Attribute | Type | Default |
|---|---|---|
| list-type | image / card | image |
| shape | square / circle / rounded | rounded |
| width | String | |
| height | String | |
| icon | String | |
| icon-size | String | |
| can-lightbox | boolean | false |
| can-delete | boolean | true |
| show-tooltip | boolean | true |

### `<output-text>` — Read-Only Text
| Attribute | Type | Default |
|---|---|---|
| icon | String | |
| icon-position | left / right | left |
| format | String | |

### `<output-list>` — Read-Only List
| Attribute | Type | Default |
|---|---|---|
| label-field-id | String | name |
| href-field-id | String | href |
| target | String | newWindow |
| separator | String | (space) |
| direction | String | column |

### `<markdown>` — Markdown Display (supports `{field}` placeholders in body)
### `<html>` — Inline HTML (supports `{field}` placeholders, use CDATA)
### `<image>` — Image Display (`url`/`data`, `shape`, `width`, `text-position`, `title`, `action-id`)
### `<alert>` — Alert/Notification
| Attribute | Type | Default |
|---|---|---|
| title | String | |
| text | String | |
| color | String | secondary |
| href | String | |
| close-button | boolean | false |

### `<hidden>` — Hidden Field (for computed values, with dependencies)
### `<search-buttons>` — Search + Reset buttons for filters
| Attribute | Type | Default |
|---|---|---|
| search-label | String | |
| reset-label | String | |
| clear-ignore | String | |

### `<search-button>` / `<clear-button>` — Individual filter buttons

---

## List/Selection Controls

### Common List Control Attributes
| Attribute | Type | Description | Default |
|---|---|---|---|
| query-id | Reference | Query for loading options | |
| label-field-id | String | Display field | name |
| value-field-id | String | ID field | id |
| format | String | Display text format | |
| search-filter-id | String | Filter for search | |
| sort-filter-id | String | Sort by field | |
| group-field-id | String | Group options by field | |
| image-field-id | String | Image field | |
| icon-field-id | String | Icon field | |
| badge-field-id | String | Badge text field | |
| badge-color-field-id | String | Badge color field | |
| badge-shape | square / circle / rounded | Badge shape | square |
| badge-position | left / right | Badge position | right |
| badge-image-field-id | String | Image field inside badge | |
| badge-image-position | left / right | Position of image in badge | left |
| badge-image-shape | square / circle / rounded | Shape of image in badge | circle |
| status-field-id | String | Status field | |
| enabled-field-id | String | Enabled flag field | |
| cache | boolean | Cache results | false |
| size | Number | Options per page | 10 |
| search | boolean | Enable search | |
| datasource | Reference | Datasource for options | |
| cleanable | boolean | Show clear button | |

### `<select>` — Simple Dropdown
| Attribute | Type | Default |
|---|---|---|
| type | single / checkboxes | single |
| cleanable | boolean | true |
| select-format | String (e.g. `{count} selected`) | |
| select-format-one | String | |
| select-format-few | String | |
| select-format-many | String | |
| description-field-id | String | |
| input-label-field-id | String | |
```xml
<select id="gender" label="Gender" query-id="genders" cleanable="true"/>
<select id="status" type="checkboxes">
    <options><option id="1" name="Active"/><option id="2" name="Inactive"/></options>
</select>
```

### `<input-select>` — Searchable Dropdown
| Attribute | Type | Default |
|---|---|---|
| type | single / multi / checkboxes | |
| reset-on-blur | boolean | true |
| max-tag-count | Number | |
| max-tag-text-length | Number | 10 |
| throttle-delay | Number (ms) | 300 |
| search-min-length | Number | 0 |
| description-field-id | String | |
| input-label-field-id | String | |
```xml
<input-select id="dept" label="Dept" query-id="departments" label-field-id="name" type="single"/>
<input-select id="skills" label="Skills" query-id="skills" type="multi" max-tag-count="3"/>
```

### `<input-select-tree>` — Tree Dropdown
| Attribute | Type | Default |
|---|---|---|
| parent-field-id | String (required) | |
| has-children-field-id | String | |
| value-field-id | String | |
| input-label-field-id | String | |
| ajax | boolean | false |
| checkboxes | boolean | false |
| checking-strategy | all / parent / child | all |
| max-tag-count | Number | |
| max-tag-text-length | Number | 10 |
| size | Number | 200 |
| throttle-delay | Number (ms) | 300 |
| search-min-length | Number | 0 |

### `<radio-group>` — Radio Buttons (`type="default/btn/tabs"`, `inline`)
```xml
<radio-group id="gender" type="btn" inline="true">
    <options><option id="M" name="Male"/><option id="F" name="Female"/></options>
</radio-group>
```

### `<checkbox-group>` — Checkbox Group (`type="default/btn"`, `inline`)
### `<auto-complete>` — Autocomplete
| Attribute | Type | Default |
|---|---|---|
| query-id | String | |
| datasource | String | |
| value-field-id | String | name |
| label-field-id | String | name |
| input-label-field-id | String | |
| search-filter-id | String | |
| tags | boolean | false |
| max-tag-text-length | Number | 10 |

### Inline `<options>` (instead of query-id)
```xml
<select id="gender"><options><option id="1" name="Male"/></options></select>
```

### Default Values for List Controls
```xml
<select id="gender"><default-value id="1" name="Male"/></select>
<input-select id="items" type="multi"><default-values><value id="1"/><value id="2"/></default-values></input-select>
```

### Pre-filters for List Options
```xml
<select id="contacts" query-id="contacts">
    <filters><eq field-id="type" value="{type.id}"/></filters>
</select>
```

---

## Interval Controls

### `<interval-field>` — Custom range
```xml
<interval-field id="range" label="Range">
    <begin><input-text domain="integer"/></begin>
    <end><input-text domain="integer"/></end>
</interval-field>
```

---

## Field Dependencies

Зависимости описываются внутри элемента `<dependencies>`. Каждый тип зависимости может быть указан не более одного раза.

```xml
<input-text id="total">
    <dependencies>
        <set-value on="price,quantity">parseInt(price) * parseInt(quantity)</set-value>
        <visibility on="type">type.id == 1</visibility>
        <enabling on="status">status == 'active'</enabling>
        <requiring on="type">type.id == 2</requiring>
        <fetch on="category" enabled="category != null"/>
        <fetch-value on="org" query-id="orgDetails" value-field-id="code">
            <filters><eq field-id="orgId" value="{org.id}"/></filters>
        </fetch-value>
        <reset on="type"/>
    </dependencies>
</input-text>
```

### Общие атрибуты (все типы зависимостей)

| Атрибут | Тип | По умолчанию | Описание |
|---|---|---|---|
| `on` | string | — | Поля через запятую, изменение которых вызывает зависимость |
| `apply-on-init` | boolean | `true` | Срабатывает ли при инициализации виджета |

---

### `<visibility>` — Видимость поля

JS-выражение (boolean). Поле отображается, когда выражение возвращает `true`.

| Атрибут | Тип | По умолчанию | Описание |
|---|---|---|---|
| `on` | string | — | Поля-источники зависимости |
| `apply-on-init` | boolean | `true` | Срабатывает ли при инициализации |
| `reset` | boolean | `false` | Сбросить значение поля при срабатывании зависимости |

---

### `<enabling>` — Доступность поля

JS-выражение (boolean). Поле активно, когда выражение возвращает `true`.

| Атрибут | Тип | По умолчанию | Описание |
|---|---|---|---|
| `on` | string | — | Поля-источники зависимости |
| `apply-on-init` | boolean | `true` | Срабатывает ли при инициализации |
| `message` | string | — | Сообщение о причине недоступности поля |

---

### `<set-value>` — Вычисление значения

JS-выражение, результат которого устанавливается как значение поля при изменении зависимых полей.

| Атрибут | Тип | По умолчанию | Описание |
|---|---|---|---|
| `on` | string | — | Поля-источники зависимости |
| `apply-on-init` | boolean | `true` | Срабатывает ли при инициализации |
| `validate` | boolean | `true` | Запускать валидацию полей при срабатывании |

---

### `<requiring>` — Обязательность поля

JS-выражение (boolean). Поле становится обязательным, когда выражение возвращает `true`.

| Атрибут | Тип | По умолчанию | Описание |
|---|---|---|---|
| `on` | string | — | Поля-источники зависимости |
| `apply-on-init` | boolean | `true` | Срабатывает ли при инициализации |
| `validate` | boolean | `true` | Запускать валидацию полей при срабатывании |

---

### `<reset>` — Сброс значения

Очищает значение поля при изменении зависимых полей. Тело элемента не используется.

| Атрибут | Тип | По умолчанию | Описание |
|---|---|---|---|
| `on` | string | — | Поля-источники зависимости |
| `apply-on-init` | boolean | `true` | Срабатывает ли при инициализации |
| `validate` | boolean | `true` | Запускать валидацию полей при срабатывании |

---

### `<fetch>` — Обновление вариантов списка

Перезагружает варианты выбора из источника данных при изменении зависимых полей. Предназначен для списковых компонентов с открытым списком (`radio-group`, `checkbox-group`), у которых есть фильтры от других полей.

| Атрибут | Тип | По умолчанию | Описание |
|---|---|---|---|
| `on` | string | — | Поля-источники зависимости |
| `apply-on-init` | boolean | `true` | Срабатывает ли при инициализации |
| `enabled` | string | `true` | Условие срабатывания (JS-выражение или плейсхолдер) |

---

### `<fetch-value>` — Получение значения из выборки

Выполняет запрос к выборке при изменении зависимых полей и устанавливает результат как значение поля.

| Атрибут | Тип | По умолчанию | Описание |
|---|---|---|---|
| `on` | string | — | Поля-источники зависимости |
| `apply-on-init` | boolean | `true` | Срабатывает ли при инициализации |
| `query-id` | string | **обязательный** | Идентификатор выборки |
| `value-field-id` | string | — | Поле выборки, значение которого устанавливается в модель (по умолчанию — вся модель) |
| `size` | integer | — | Размер выборки |
| `enabled` | string | `true` | Условие срабатывания (JS-выражение или плейсхолдер) |

Дочерний элемент `<filters>` задаёт предустановленные фильтры запроса.

**Типы фильтров:** `<eq>`, `<in>`, `<like>`, `<likeStart>`, `<isNull>`, `<isNotNull>`, `<more>`, `<less>`, `<notEq>`, `<notIn>`, `<contains>`

**Атрибуты фильтров:**

| Атрибут | Тип | Описание |
|---|---|---|
| `field-id` | string | **обязательный** — поле виджета, по которому выполняется фильтрация |
| `value` | string | Значение фильтра (поддерживает плейсхолдеры, например `{org.id}`) |
| `datasource` | string | Идентификатор источника данных для фильтрации |
| `model` | clientModel | Модель виджета для фильтрации |
| `param` | string | Параметр фильтра |
| `reset-on-change` | boolean | Сбрасывать значение при изменении модели (по умолчанию `false`) |
| `required` | boolean | Обязательность предустановленного фильтра |

## Inline Validations
```xml
<input-text id="email">
    <validations>
        <condition id="emailFormat" message="Invalid email">
            /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
        </condition>
    </validations>
</input-text>
```

## See Also
- `widget.md` — widgets that contain fields (form, table filters)
- `fieldset.md` — organizing fields into groups and grids
- `query.md` — query fields that map to UI fields
- `object.md` — server-side validations
- `snippets.md` — dependent selects, computed fields examples
