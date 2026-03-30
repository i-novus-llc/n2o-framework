# Fields / Input Controls (control-3.0)

Schema: `http://n2oapp.net/framework/config/schema/control-3.0`
Fields are placed inside `<fields>` of a form widget, or inside `<filters>` of a table.

## Base Field Attributes (inherited by all controls)
| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Field identifier (required) | |
| label | String | Field label | From query field name |
| help | String | Help tooltip text | |
| help-trigger | hover/click | Help tooltip trigger | click |
| placeholder | String | Input placeholder | |
| required | boolean | Required field | false |
| visible | boolean / String | Visibility (can be JS) | true |
| enabled | boolean / String | Enabled (can be JS) | true |
| no-label | boolean | Hide label text | false |
| no-label-block | boolean | Hide label block entirely | false |
| description | String | Description under field | |
| default-value | String | Default value | |
| param | String | URL parameter for default | |
| ref-model | resolve/edit/filter/multi/datasource | Model for init | resolve |
| ref-datasource | Reference | Datasource for init | current widget |
| ref-field-id | String | Field for initialization | |
| copied | boolean | Copy on merge defaults | true |
| domain | String | Data type | |
| src | String | Custom React component | |
| class / style | String | CSS | |

---

## Simple Input Controls

### `<input-text>` — Text/Number Input
| Attribute | Type | Default |
|---|---|---|
| length | Number (max chars) | |
| precision | Number (decimal) | 8 for numeric |
| max / min | Number | |
| step | String | 1 (int), 0.01 (numeric) |
| measure | String (unit suffix) | |
| autocomplete | on / off | off |
```xml
<input-text id="name" label="Name" required="true" length="100"/>
<input-text id="age" label="Age" domain="integer" min="0" max="150"/>
<input-text id="salary" label="Salary" domain="numeric" precision="2" measure="₽"/>
```

### `<text-area>` — Multi-line Text
| Attribute | Type |
|---|---|
| min-rows / max-rows | Number |
| max-length | Number |

### `<password>` — Password Input
| Attribute | Type |
|---|---|
| eye | boolean (show/hide toggle) |
| length | Number |

### `<date-time>` — Date/Time Picker
| Attribute | Type | Default |
|---|---|---|
| date-format | DD.MM.YYYY / DD/MM/YYYY / etc. | DD.MM.YYYY |
| time-format | HH:mm / HH:mm:ss | |
| min / max | String (ISO 8601) | |
| utc | boolean | false |
```xml
<date-time id="birthday" label="Birthday" date-format="DD.MM.YYYY"/>
<date-time id="createdAt" label="Created" date-format="DD.MM.YYYY" time-format="HH:mm"/>
```

### `<date-interval>` — Date Range
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
| Attribute | Type |
|---|---|
| mask | String (e.g. `+7 (999) 999-99-99`) |

### `<email>` — Email Input (preset mask)
### `<phone>` — Phone Input (`country="RU, KZ"`)
### `<snils>` — SNILS Input (Russian social number)

### `<input-money>` — Money Input
| Attribute | Type |
|---|---|
| prefix / suffix | String (currency symbol) |
| thousands-separator | String |
| decimal-separator | String |
| integer-limit | Number |

### `<slider>` — Range Slider
| Attribute | Type |
|---|---|
| min / max | Number |
| step | Number |
| measure | String |
| vertical | boolean |

### `<number-picker>` — +/- Number Selector
### `<rating>` — Star Rating (`max`, `half`)
### `<time-picker>` — Time Selector (`mode="hour,minute"`, `time-format="HH:mm"`)
### `<code-editor>` — Code Editor (`language`, `min-lines`, `max-lines`)
### `<text-editor>` — Rich Text Editor (WYSIWYG)
### `<progress>` — Progress Bar (`max`, `color`, `animated`, `striped`)
### `<status>` — Status Indicator (`color`, `text`)
### `<file-upload>` — File Upload (`multi`, `ajax`, `upload-url`, `delete-url`, `value-field-id`, `label-field-id`, `url-field-id`, `accept`, `show-size`)
### `<image-upload>` — Image Upload (`shape`, `width`, `height`, `can-lightbox`)
### `<output-text>` — Read-Only Text (`icon`)
### `<output-list>` — Read-Only List (`label-field-id`, `direction`, `separator`)
### `<markdown>` — Markdown Display (supports `{field}` placeholders in body)
### `<html>` — Inline HTML (supports `{field}` placeholders, use CDATA)
### `<image>` — Image Display (`url`/`data`, `shape`, `width`, `height`, `text-position`)
### `<alert>` — Alert/Notification (`title`, `text`, `color`, `close-button`)
### `<hidden>` — Hidden Field (for computed values, with dependencies)
### `<search-buttons>` — Search + Reset buttons for filters
### `<search-button>` / `<clear-button>` — Individual filter buttons

---

## List/Selection Controls

### Common List Control Attributes
| Attribute | Type | Description | Default |
|---|---|---|---|
| query-id | Reference | Query for loading options | |
| label-field-id | String | Display field | |
| value-field-id | String | ID field | id |
| search-filter-id | String | Filter for search | label-field-id |
| sort-filter-id | String | Sort by field | label-field-id |
| group-field-id | String | Group options by field | |
| image-field-id | String | Image field | |
| icon-field-id | String | Icon field | |
| badge-field-id / badge-color-field-id | String | Badge fields | |
| enabled-field-id | String | Enabled flag field | |
| cache | boolean | Cache results | false |
| size | Number | Options per page | 10 |
| search | boolean | Enable search | auto |
| datasource | Reference | Datasource for options | |
| cleanable | boolean | Show clear button | |

### `<select>` — Simple Dropdown
| Attribute | Type |
|---|---|
| type | single / checkboxes |
| select-format | String (e.g. `{count} selected`) |
```xml
<select id="gender" label="Gender" query-id="genders" cleanable="true"/>
<select id="status" type="checkboxes">
    <options><option id="1" name="Active"/><option id="2" name="Inactive"/></options>
</select>
```

### `<input-select>` — Searchable Dropdown
| Attribute | Type |
|---|---|
| type | single / multi / checkboxes |
| max-tag-count | Number (for multi) |
| throttle-delay | Number (ms) |
| description-field-id | String |
```xml
<input-select id="dept" label="Dept" query-id="departments" label-field-id="name" type="single"/>
<input-select id="skills" label="Skills" query-id="skills" type="multi" max-tag-count="3"/>
```

### `<input-select-tree>` — Tree Dropdown
| Attribute | Type |
|---|---|
| parent-field-id | String |
| has-children-field-id | String |
| checkboxes | boolean |
| checking-strategy | child / parent / all |

### `<radio-group>` — Radio Buttons (`type="default/btn/tabs"`, `inline`)
```xml
<radio-group id="gender" type="btn" inline="true">
    <options><option id="M" name="Male"/><option id="F" name="Female"/></options>
</radio-group>
```

### `<checkbox-group>` — Checkbox Group (`inline`)
### `<auto-complete>` — Autocomplete (`tags` for free text)

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
```xml
<input-text id="total">
    <dependencies>
        <set-value on="price,quantity">parseInt(price) * parseInt(quantity)</set-value>
        <visibility on="type">type.id == 1</visibility>
        <enabling on="status">status == 'active'</enabling>
        <requiring on="type">type.id == 2</requiring>
        <fetch-value on="org" query-id="orgDetails" value-field-id="code">
            <filters><eq field-id="orgId" value="{org.id}"/></filters>
        </fetch-value>
        <reset on="type"/>
    </dependencies>
</input-text>
```
| Element | Description |
|---|---|
| `<set-value on="fields">` | JS expression to compute value |
| `<visibility on="fields">` | JS condition for visibility |
| `<enabling on="fields">` | JS condition for enabled state |
| `<requiring on="fields">` | JS condition for required state |
| `<fetch-value on="field" query-id="..." value-field-id="...">` | Fetch data from query when field changes |
| `<reset on="fields"/>` | Clear field when dependencies change |

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
