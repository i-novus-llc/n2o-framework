# Table Settings (table-settings-1.0)

Schema: `http://n2oapp.net/framework/config/schema/table-settings-1.0`
Namespace prefix: `ts` (conventional)

Table settings are **system buttons** placed inside `<toolbar>` of a `<table>` widget. They provide built-in table management actions (column visibility, export, filter toggle, etc.) without writing custom button code.

## Usage

Declare the namespace on the page root, then use `ts:` elements inside `<toolbar>`:
```xml
<simple-page xmlns="http://n2oapp.net/framework/config/schema/page-4.0"
             xmlns:ts="http://n2oapp.net/framework/config/schema/table-settings-1.0">
    <table>
        <toolbar place="topRight">
            <ts:columns/>
            <ts:export/>
            <ts:refresh/>
        </toolbar>
        <columns>...</columns>
    </table>
</simple-page>
```

## Common Attributes (all `ts:` elements inherit these)
| Attribute | Type | Description |
|---|---|---|
| label | String | Button label |
| icon | String | FontAwesome icon (https://fontawesome.com/v6/icons/) |
| description | String | Tooltip text on hover |
| color | primary/secondary/success/danger/warning/info/link/light/dark | Button color (supports placeholders) |
| src | String | Custom React component |
| class | String | CSS class |
| style | String | CSS style |

---

## `<ts:columns>` — Show/Hide Columns
Opens a panel for toggling column visibility.

| Attribute | Type | Description |
|---|---|---|
| default-value | String (comma-sep) | Column IDs visible by default |
| locked | String (comma-sep) | Column IDs that cannot be toggled |

```xml
<ts:columns default-value="id,name,status" locked="id"/>
```

---

## `<ts:export>` — Export Table Data
Exports table data to a file. By default opens a modal for format/charset/size selection.

| Attribute | Type | Description | Default |
|---|---|---|---|
| url | String | URL to external export service (if blank — built-in export) | |
| format | String (comma-sep) | Available formats to choose from | |
| default-format | csv/xlsx | Pre-selected format | csv |
| default-charset | UTF-8/Cp1251 | Pre-selected charset | utf-8 |
| default-size | all/page | Export all records or current page only | all |
| show-modal | boolean | Show settings modal; `false` = download immediately with defaults | true |
| filename | String | Name of downloaded file | |

```xml
<!-- Full export with modal -->
<ts:export default-format="xlsx" default-charset="UTF-8" filename="report"/>

<!-- Immediate download, no modal, external service -->
<ts:export url="${n2o.export.url}" default-format="csv" default-size="page" show-modal="false"/>
```

---

## `<ts:filters>` — Toggle Filter Panel
Shows or hides the table filter panel.

Only common attributes (label, icon, description, color, src, class, style).

```xml
<ts:filters/>
```

---

## `<ts:refresh>` — Refresh Table
Reloads table data from the datasource.

Only common attributes.

```xml
<ts:refresh/>
```

---

## `<ts:resize>` — Change Page Size
Opens a selector to change the number of displayed rows.

| Attribute | Type | Description |
|---|---|---|
| size | String (comma-sep numbers) | Available page sizes to choose from |

```xml
<ts:resize size="5,10,20,50"/>
```

---

## `<ts:word-wrap>` — Toggle Word Wrap
Toggles text wrapping in table cells.

Only common attributes.

```xml
<ts:word-wrap/>
```

---

## `<ts:reset-settings>` — Reset Table Settings
Resets all user-customized table settings (column visibility, page size, word-wrap) to defaults.

Only common attributes.

```xml
<ts:reset-settings/>
```

---

## Full Example
```xml
<simple-page xmlns="http://n2oapp.net/framework/config/schema/page-4.0"
             xmlns:ts="http://n2oapp.net/framework/config/schema/table-settings-1.0"
             name="Employee List">
    <table>
        <datasource query-id="employees"/>
        <filters place="top">
            <row><col size="4"><input-text id="name" label="Name"/></col></row>
        </filters>
        <columns>
            <column text-field-id="id" label="ID"/>
            <column text-field-id="name" label="Name"/>
            <column text-field-id="department" label="Department"/>
            <column text-field-id="status" label="Status"/>
        </columns>
        <toolbar place="topLeft">
            <button label="Create" color="primary"><show-modal page-id="employeeCreate"/></button>
        </toolbar>
        <toolbar place="topRight">
            <group>
                <ts:filters/>
                <ts:columns default-value="id,name,department,status" locked="id"/>
                <ts:refresh/>
                <ts:resize size="5,10,25,50"/>
                <ts:word-wrap/>
                <ts:export default-format="xlsx" default-charset="UTF-8"/>
                <ts:reset-settings/>
            </group>
        </toolbar>
    </table>
</simple-page>
```

## See Also
- `widget.md` — `<table>` widget, `<toolbar>`, `<columns>` configuration
- `button.md` — regular toolbar buttons
