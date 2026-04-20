# Widgets (widget-5.0)

Schema: `http://n2oapp.net/framework/config/schema/widget-5.0`
Widgets are visual components placed inside regions.

## Base Widget Attributes (inherited by all)
| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Widget id within page | Generated wgt[idx] |
| ref-id | Reference | Parent widget file | |
| src | String | React component | Per widget type |
| class | String | CSS class | |
| style | String | CSS style | |
| datasource | Reference | Datasource id | |
| fetch-on-init | boolean | Fetch data on init | |
| fetch-on-visibility | boolean | Fetch when visible | |
| visible | boolean | Widget visibility | |
| auto-focus | boolean | Auto-focus | true |

Setting: `n2o.api.widget.auto_focus=true`

**Common body elements** (most widgets): `<dependencies>`, `<datasource>`, `<toolbar>`, `<actions>`

---

## `<form>` — Form Widget
| Attribute | Type | Description | Default |
|---|---|---|---|
| mode | one-model/two-models | Form mode (number of models on the client) | one-model |
| unsaved-data-prompt | boolean | Show a warning about unsaved data | false |

Settings: `n2o.api.widget.form.unsaved_data_prompt=false`, `n2o.api.widget.form.size=1`
Body: `<fields>`, `<dependencies>`, `<datasource>`, `<toolbar>`, `<actions>`
```xml
<form name="Employee" object-id="employee">
    <fields>
        <input-text id="name" label="Name" required="true"/>
        <date-time id="birthday" label="Birthday"/>
        <select id="department" label="Department" query-id="departments" label-field-id="name"/>
    </fields>
    <toolbar place="bottomRight">
        <button label="Save" color="primary"><invoke operation-id="save" close-on-success="true"/></button>
        <button label="Cancel"><close/></button>
    </toolbar>
</form>
```

---

## `<table>` — Table Widget
| Attribute | Type | Default |
|---|---|---|
| selection | none/active/radio/checkbox | active |
| width | String (px/em/rem/%) | |
| height | String (px/em/rem) | |
| text-wrap | boolean | true |
| auto-select | boolean | true |
| children | collapse/expand | collapse |
| sticky-header | boolean | false |
| sticky-footer | boolean | false |
| scrollbar-position | top/bottom | bottom |

Settings:
```
n2o.api.widget.table.size=10
n2o.api.widget.table.text_wrap=true
n2o.api.widget.table.check_on_select=false
n2o.api.widget.table.auto_select=true
n2o.api.widget.table.children.toggle=collapse
n2o.api.widget.table.selection=active
n2o.api.widget.table.checkboxes=false
n2o.api.widget.table.sticky_header=false
n2o.api.widget.table.sticky_footer=false
n2o.api.widget.table.scrollbar_position=bottom
```

Body: `<columns>`, `<rows>`, `<filters>`, `<pagination>`, `<dependencies>`, `<datasource>`, `<toolbar>`, `<actions>`

### `<column>` Attributes
| Attribute | Type | Default |
|---|---|---|
| id | String | text-field-id value |
| src | String | React component |
| text-field-id | String | Query field for display |
| tooltip-field-id | String | Tooltip field |
| visible | boolean | true |
| width | String (px/em/rem/%) | |
| resizable | boolean | false |
| label | String | From query field name |
| icon | String (FontAwesome) | |
| sorting-field-id | String | Sort field |
| sorting-direction | asc / desc | |
| fixed | left / right | |
| class | String | CSS class |
| style | String | CSS style |
| alignment | left/right/center | left |
| content-alignment | left/right/center | same as alignment |

Settings: `n2o.api.widget.column.alignment=left`, `n2o.api.widget.table.column.resizable=false`

Body: A Cell component + `<dependencies>` → `<visibility>`
```xml
<column text-field-id="name" label="Name" sorting-direction="asc" width="200px">
    <dependencies><visibility on="showName">showName == true</visibility></dependencies>
</column>
```

### `<filter-column>` — Column with inline filter
Same attributes as `<column>`. Body: `<filter>` (contains a field control) + cell.
```xml
<filter-column text-field-id="name" label="Name" sorting-field-id="name">
    <filter><input-text/></filter>
</filter-column>
```

### `<multi-column>` — Multi-level column header
| Attribute | Type | Default |
|---|---|---|
| label | String | |
| src | String | TextTableHeader |
| class | String | CSS class |
| style | String | CSS style |
| alignment | left/right/center | center |
| content-alignment | left/right/center | |
| width | String | Column width |
| fixed | left/right | Fixed column position |

### `<dnd-column>` — Drag-n-drop reorderable column
| Attribute | Type | Default |
|---|---|---|
| move-mode | settings/table/all | all |
| fixed | left/right | |

### `<rows>` — Row configuration
| Attribute | Type | Description |
|---|---|---|
| src | String | React component |
| class | String | CSS class (supports `{field}` placeholders) |
| style | String | CSS style |

Body: `<switch>`, `<click>` (action), `<overlay>` (toolbar on hover)

**`<click>` attributes:**
| Attribute | Type | Description |
|---|---|---|
| action-id | String | Widget action to invoke |
| enabled | String | JS condition for availability |

```xml
<rows class="{type==1?'text-muted':''}">
    <click><open-page page-id="detail"/></click>
    <overlay class="top"><toolbar><button label="Quick Edit"/></toolbar></overlay>
</rows>
```

### `<filters>` — Table filter panel
| Attribute | Type | Default |
|---|---|---|
| place | top / left | top |
| fetch-on-change | boolean | false |
| fetch-on-clear | boolean | true |
| fetch-on-enter | boolean | true |
| datasource | String | |

Body: Field controls and fieldsets
```xml
<filters place="top">
    <input-text id="name" label="Name" filter-id="nameLike"/>
    <select id="dept" label="Dept" query-id="departments" filter-id="deptId"/>
    <search-buttons/>
</filters>
```

### `<pagination>`
| Attribute | Type | Default |
|---|---|---|
| src | String | React component |
| place | topLeft/topRight/bottomLeft/bottomRight/topCenter/bottomCenter | bottomLeft |
| show-count | always/by-request/never | always |
| show-last | boolean | true |
| prev | boolean | false |
| next | boolean | false |
| prev-label | String | Prev button text |
| prev-icon | String (FontAwesome) | Prev button icon |
| next-label | String | Next button text |
| next-icon | String (FontAwesome) | Next button icon |
| class | String | CSS class |
| style | String | CSS style |
| routable | boolean | false |

### Full Table Example
```xml
<table name="Employees" datasource="ds1" size="10">
    <filters>
        <input-text id="name" label="Name" filter-id="nameLike"/>
        <search-buttons/>
    </filters>
    <columns>
        <column text-field-id="name" label="Name" sorting-direction="asc"/>
        <column text-field-id="department.name" label="Department"/>
        <column text-field-id="birthday" label="Birthday"><text format="date DD.MM.YYYY"/></column>
    </columns>
    <rows><click><open-page page-id="card"/></click></rows>
    <toolbar place="topLeft" generate="crud"/>
    <pagination/>
</table>
```

---

## `<list>` — List Widget
Settings: `n2o.api.widget.list.size=5`
Body: `<content>`, `<rows>`, `<pagination>`

Content areas: `<left-top>`, `<left-bottom>`, `<header>`, `<body>`, `<sub-header>`, `<right-top>`, `<right-bottom>`, `<extra>` — each with `text-field-id` and a Cell inside.
```xml
<list name="My List" datasource="myList">
    <content>
        <left-top text-field-id="image"><image width="50px"/></left-top>
        <header text-field-id="name"><text/></header>
        <body text-field-id="description"><text/></body>
    </content>
    <rows><click><open-page page-id="detail"/></click></rows>
    <pagination/>
</list>
```

---

## `<cards>` — Cards Widget
| Attribute | Type | Default |
|---|---|---|
| vertical-align | center/top/bottom | top |
| height | String (px/em/rem) | |

Settings: `n2o.api.widget.cards.size=10`, `n2o.api.widget.cards.vertical_align=top`
Body: `<content>` → `<col>` → `<block>` (text-field-id + Cell)

---

## `<tiles>` — Tiles Widget
| Attribute | Type | Default |
|---|---|---|
| cols-sm | 1/2 | 1 |
| cols-md | 1/2/3/4/6 | 2 |
| cols-lg | 1/2/3/4/6/12 | 4 |
| height / width | String | |

Settings: `n2o.api.widget.tiles.colsLg=4`, `n2o.api.widget.tiles.size=10`

---

## `<tree>` — Tree Widget
| Attribute | Type | Default |
|---|---|---|
| parent-field-id | Reference | |
| value-field-id | Reference | |
| label-field-id | Reference | |
| icon-field-id | Reference | |
| has-children-field-id | Reference | |
| badge-field-id / badge-color-field-id | Reference | |
| badge-position | left/right | right |
| badge-shape | rounded/circle/square | square |
| badge-image-field-id | Reference | Badge image |
| badge-image-position | left/right | left |
| badge-image-shape | rounded/circle/square | circle |
| image-field-id | Reference | |
| multi-select | boolean | false |
| checkboxes | boolean | false |
| ajax | boolean | false |

Note: Records must self-reference via `parent-field-id`.
```xml
<tree parent-field-id="parentId" label-field-id="name" value-field-id="id" datasource="ds1" ajax="true"/>
```

---

## `<calendar>` — Calendar Widget
| Attribute | Type | Default | Required |
|---|---|---|---|
| height | String | | |
| default-date | String | current | |
| default-view | day/week/workWeek/month/agenda | month | |
| views | String (comma-sep) | | ! |
| min-time / max-time | String (HH:mm:ss) | | |
| mark-days-off | boolean | true | |
| selectable | boolean | true | |
| step | Number (minutes) | 30 | |
| timeslot-count | Number | 2 | |
| title-field-id | String | | ! |
| tooltip-field-id | String | | ! |
| start-field-id | String | | ! |
| end-field-id | String | | ! |
| cell-color-field-id | String | | ! |
| disabled-field-id | String | | ! |

Body: `<resources>`, `<action-on-select-slot>`, `<action-on-select-event>`, `<formats>`

---

## `<chart>` — Chart Widget
| Attribute | Type | Description |
|---|---|---|
| width | String (px) | Chart width |
| height | String (px) | Chart height |

Body: `<areas>`, `<bars>`, `<lines>`, `<pie>`

### Shared Axes Attributes (`<areas>`, `<bars>`, `<lines>`)
| Attribute | Type | Description | Default |
|---|---|---|---|
| x-field-id | String | Field for X axis | |
| x-position | top/bottom | X axis position | bottom |
| x-has-label | boolean | Show X axis label | false |
| y-field-id | String | Field for Y axis | |
| y-position | left/right | Y axis position | left |
| y-has-label | boolean | Show Y axis label | false |
| y-min | Number | Y axis minimum value | |
| y-max | Number | Y axis maximum value | |
| grid-horizontal | boolean | Show horizontal grid lines | true |
| grid-vertical | boolean | Show vertical grid lines | true |
| grid-stroke-dasharray | String | Grid line dash pattern | |
| tooltip-separator | String | Separator in tooltip | |
| legend-icon-type | line/square/rect/circle/cross/diamond/star/triangle/wye | Legend icon type | line |

### `<area>` / `<bar>` / `<line>` Attributes
| Attribute | Type | Description | Default |
|---|---|---|---|
| field-id | String | Data field (required) | |
| label | String | Series label | |
| color | String | Series color | |
| has-label | boolean | Show data labels | false |
| line-type | basis/basisClosed/basisOpen/linear/linearClosed/natural/monotoneX/monotoneY/monotone/step/stepBefore/stepAfter | Line type (`<area>`, `<line>`) | linear |

### `<pie>` Attributes
| Attribute | Type | Description | Default |
|---|---|---|---|
| name-field-id | String | Field for slice label | |
| value-field-id | String | Field for slice value | value |
| tooltip-field-id | String | Field for tooltip | |
| color | String | Slice colors | |
| has-label | boolean | Show data labels | false |
| inner-radius | Number | Inner radius (donut chart) | 0 |
| outer-radius | Number | Outer radius | |
| start-angle | Number | Start angle in degrees | 0 |
| end-angle | Number | End angle in degrees | 360 |
| center-x | Number | Center X coordinate | |
| center-y | Number | Center Y coordinate | |

## `<html>` — HTML Content Widget
```xml
<html><content url="page.html"/></html>
<html><content><![CDATA[<div>Hello</div>]]></content></html>
```

## `<widget>` — Custom React Widget
```xml
<widget src="MyWidget" ext:prop1="value1"/>
```

---

## Widget `<toolbar>`
| Attribute | Type | Default |
|---|---|---|
| class / style | String | |
| place | topLeft/topRight/bottomLeft/bottomRight/topCenter/bottomCenter | topLeft |
| generate | crud/create/update/delete/close/submit (comma-sep) | |

Body: `<button>`, `<sub-menu>`, `<group>`
```xml
<toolbar place="topLeft" generate="crud">
    <button label="Custom"/>
    <group generate="close"/>
</toolbar>
```

## Widget `<dependencies>`
```xml
<dependencies>
    <visibility datasource="ds1">type.id == 1</visibility>
    <enabling datasource="ds1">status == 'active'</enabling>
</dependencies>
```

## Inline `<datasource>` (inside widget)
Default id = widget id. See datasource.md for full docs.
```xml
<form>
    <datasource query-id="data" object-id="data">
        <filters><eq field-id="id" param="person_id"/></filters>
    </datasource>
    <fields>...</fields>
</form>
```

## See Also
- `field.md` — input controls for forms and filters
- `cell.md` — cell renderers for table columns
- `button.md` — toolbar buttons and actions
- `datasource.md` — data source configuration
- `action.md` — actions triggered by widgets
- `snippets.md` — common widget patterns
