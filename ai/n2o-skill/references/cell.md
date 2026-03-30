# Table Cells

Cells define how table column values are rendered. Placed inside `<column>` elements.

## Common Attributes (all cells)
| Attribute | Type | Description |
|---|---|---|
| src | String | Custom React component |
| class | String | CSS class (supports `{field}` placeholders) |
| style | String | CSS style (supports `{field}` placeholders) |
| visible | boolean | Cell visibility |

## `<text>` — Text Cell
| Attribute | Type | Description |
|---|---|---|
| format | String | Format pattern: `date DD.MM.YYYY`, `number 0,0.00`, `password` |
| subtext-field-id | Reference | Field for secondary text |
| subtext-format | String | Format for secondary text |
| icon | String | FontAwesome icon class |
| icon-position | left / right | Icon position (default: left) |
| switch | Element | Color/style switching by value (see below) |
```xml
<column text-field-id="name" label="Name"><text/></column>
<column text-field-id="birthday" label="Birthday"><text format="date DD.MM.YYYY"/></column>
<column text-field-id="salary"><text format="number 0,0.00"/></column>
<column text-field-id="name"><text icon="fa fa-user" icon-position="left"/></column>
```

## `<badge>` — Badge Cell
| Attribute | Type | Description |
|---|---|---|
| position | left / right | Badge position relative to text |
| shape | square / circle / rounded | Badge shape (default: square) |
| color | String | Badge color (supports `{field}` placeholders) |
| text | String | Text outside badge |
| format | String | Format for text inside badge |
| text-format | String | Format for text outside badge |
| image-field-id | Reference | Field for image in badge |
| image-position | left / right | Image position in badge (default: left) |
| image-shape | square / circle / rounded | Image shape in badge (default: circle) |
| switch | Element | Color/style switching by value |
```xml
<column text-field-id="status.name" label="Status">
    <badge color="{status.color}" shape="rounded" position="left"/>
</column>
```

## `<icon>` — Icon Cell
| Attribute | Type | Description |
|---|---|---|
| icon | String | FontAwesome icon class (supports `{field}` placeholders) |
| text | String | Text next to icon |
| position | left / right | Icon position relative to text (default: left) |
| switch | Element | Icon/style switching by value |
```xml
<column text-field-id="type.name"><icon icon="{type.icon}" position="left"/></column>
```

## `<image>` — Image Cell
| Attribute | Type | Description |
|---|---|---|
| data | String | Image data field (base64 or URL with placeholders) |
| shape | square / circle / rounded | Image shape (default: square) |
| width | String | Width (px, em, rem, %) |
| title | String | Title text |
| description | String | Subtitle text |
| text-position | left / right / top / bottom | Text position (default: right) |
| action-id | String | Widget action on click |
| statuses | Element | Status indicators |
| action | Element | Inline action definition |
```xml
<column text-field-id="fullName">
    <image data="{photoUrl}" shape="circle" width="40px"/>
</column>
```

## `<link>` — Clickable Link Cell
| Attribute | Type | Description |
|---|---|---|
| url | String | URL (supports `{field}` placeholders) |
| target | _self / _blank / application | Link open mode |
| icon | String | FontAwesome icon class |
| action-id | String | Widget action reference |
Body: Action element (optional, overrides url)
```xml
<column text-field-id="name"><link url="/persons/{id}"/></column>
<column text-field-id="name"><link><open-page page-id="detail"/></link></column>
```

## `<list>` — List Cell
| Attribute | Type | Description |
|---|---|---|
| color | String | Element color (supports placeholders) |
| size | String | Number of elements for grouping |
| label-field-id | String | Display field for object arrays |
| inline | boolean | Display elements on one line (default: false) |
| separator | String | Separator between elements |
Body: `<text>`, `<badge>`, `<link>`, or `<cell>` for item rendering
```xml
<column text-field-id="tags">
    <list label-field-id="name" inline="true" separator=", ">
        <badge color="{color}"/>
    </list>
</column>
```

## `<checkbox>` — Checkbox Cell
| Attribute | Type | Description |
|---|---|---|
| enabled | boolean | Checkbox availability |
| action-id | String | Widget action on change |
Body: Action element (e.g., `<invoke>`)
```xml
<column text-field-id="active" label="Active">
    <checkbox enabled="true">
        <invoke operation-id="toggleActive"/>
    </checkbox>
</column>
```

## `<rating>` — Star Rating Cell
| Attribute | Type | Description |
|---|---|---|
| max | integer | Maximum value (default: 5) |
| half | boolean | Allow half values (default: false) |
| readonly | boolean | Read-only mode (default: true) |
| show-tooltip | boolean | Show tooltip (default: false) |
| action-id | String | Widget action on change |
```xml
<column text-field-id="rating"><rating max="5" half="true" readonly="true"/></column>
```

## `<progress>` — Progress Bar Cell
| Attribute | Type | Description |
|---|---|---|
| color | String | Color (supports placeholders): primary/secondary/success/danger/warning/info |
| active | boolean | Animated loading (default: false) |
| striped | boolean | Striped style (default: false) |
| size | large / normal / small | Progress bar size (default: normal) |
```xml
<column text-field-id="progress"><progress color="success" striped="true" size="normal"/></column>
```

## `<edit>` — Editable Cell
| Attribute | Type | Description |
|---|---|---|
| format | String | Text format |
| enabled | String | Edit availability (default: true) |
| action-id | String | Action on edit complete |
Body: Control elements (`<input-text>`, `<date-time>`, `<input-select>`, `<text-area>`, `<field>`), `<action>`
```xml
<column text-field-id="name">
    <edit>
        <input-text/>
        <action><invoke operation-id="save"/></action>
    </edit>
</column>
```

## `<tooltip-list>` — Tooltip List Cell
| Attribute | Type | Description |
|---|---|---|
| label | String | Universal field label |
| label-few | String | Label for few elements |
| label-many | String | Label for many elements |
| dashed-label | boolean | Dashed underline (default: true) |
| trigger | hover / click / focus | Expand trigger (default: hover) |
```xml
<column text-field-id="participants">
    <tooltip-list label="{count} participants" trigger="hover"/>
</column>
```

## `<file-upload>` — File Upload Cell
| Attribute | Type | Description |
|---|---|---|
| multi | boolean | Multiple file support (default: false) |
| ajax | boolean | Ajax upload (default: true) |
| show-size | boolean | Show file size (default: true) |
| upload-url | String | File upload URL |
| delete-url | String | File delete URL |
| value-field-id | String | Field with file ID |
| label-field-id | String | Field with file name |
| message-field-id | String | Response field with status message (default: message) |
| url-field-id | String | Field with download URL |
| request-param | String | Multipart form data field name |
| accept | String | Allowed file extensions (comma-separated) |
| label | String | Default cell text |
| upload-icon | String | Upload icon (FontAwesome) |
| delete-icon | String | Delete icon (FontAwesome) |
```xml
<column text-field-id="document">
    <file-upload upload-url="/files/upload" accept=".pdf,.doc" label="Upload"/>
</column>
```

## `<cell>` — Custom Cell
| Attribute | Type | Description |
|---|---|---|
| src | String | Custom React component (required) |
| action-id | String | Widget action on click |
| * | any | Any custom attributes |
```xml
<column text-field-id="custom"><cell src="MyCustomCell" my-prop="value"/></column>
```

## `<switch>` — Cell Type Switcher
Switches between different cell types based on field value.
| Attribute | Type | Description |
|---|---|---|
| value-field-id | Reference | Field to compare (required) |
Body: `<case value="...">` with cell inside, `<default>` for fallback
```xml
<column text-field-id="type">
    <switch value-field-id="type">
        <case value="text"><text/></case>
        <case value="image"><image shape="circle"/></case>
        <default><badge color="secondary"/></default>
    </switch>
</column>
```

## `<switch>` inside cells — Style Switcher
Used inside `<text>`, `<badge>`, or `<icon>` for value-dependent styling.
| Attribute | Type | Description |
|---|---|---|
| value-field-id | Reference | Field to compare |
Body: `<case value="...">`, `<default>` — styling applied via CSS class/style on parent
```xml
<column text-field-id="status.name">
    <text class="{statusClass}">
        <switch value-field-id="status.id">
            <case value="1"/><!-- styling via class -->
            <case value="2"/>
            <default/>
        </switch>
    </text>
</column>
```

## `<toolbar>` — Buttons in a Cell
```xml
<column label="Actions">
    <toolbar>
        <button label="Edit" icon="fa fa-edit"><open-page page-id="editCard"/></button>
        <button label="Delete" icon="fa fa-trash" color="danger">
            <invoke operation-id="delete" confirm="true"/>
        </button>
    </toolbar>
</column>
```

## See Also
- `widget.md` — table column configuration
- `button.md` — toolbar cells with action buttons
- `query.md` — query fields displayed in cells
