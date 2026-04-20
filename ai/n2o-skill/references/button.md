# Buttons

Buttons are placed inside `<toolbar>` of widgets or pages. They trigger actions.

## `<button>` Attributes
| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Button identifier | |
| label | String | Button text | |
| icon | String | FontAwesome icon | |
| icon-position | left/right | Icon position relative to label | left |
| color | primary/secondary/success/danger/warning/info/link/light/dark | Button color | |
| badge | String | Badge text | |
| badge-color | primary/secondary/success/danger/warning/info/light/dark | Badge color | |
| badge-position | left/right | Badge position | right |
| badge-shape | square/circle/rounded | Badge shape | circle |
| badge-image | String | Badge image field | |
| badge-image-position | left/right | Badge image position | left |
| badge-image-shape | square/circle/rounded | Badge image shape | circle |
| action-id | Reference | Reference to named action | |
| visible | String | Visibility condition (boolean or JS expression) | true |
| enabled | String | Enabled condition (boolean or JS expression) | |
| disable-on-empty-model | true/false/auto | Disable when no data | |
| model | resolve/edit/filter/multi/datasource | Source model | |
| datasource | Reference | Source datasource | |
| description | String | Tooltip text on hover | |
| tooltip-position | top/bottom/left/right | Tooltip position | bottom |
| validate | boolean | Validate before action | true |
| validate-datasources | String (comma-sep) | Datasources to validate | |
| generate | crud/create/update/delete/close/submit | Auto-generate standard button | |
| class | String | CSS class | |
| style | String | CSS style | |
| rounded | boolean | Rounded corners | false |
| src | String | Custom React component | |

Body: Action element (invoke, open-page, show-modal, confirm, etc.) and optional `<dependencies>`.

```xml
<button label="Save" color="primary" icon="fa fa-save" validate="true">
    <invoke operation-id="save" close-on-success="true"/>
</button>
<button label="Delete" color="danger">
    <confirm text="Are you sure?" type="modal">
        <ok label="Delete" color="danger"/>
        <cancel label="Cancel"/>
    </confirm>
    <invoke operation-id="delete" close-on-success="true"/>
</button>
<button label="Cancel"><close/></button>
<button label="View" action-id="view"/><!-- references named action -->
```

## `<sub-menu>` — Dropdown Button
Same attributes as `<button>` (common attributes), plus:
| Attribute | Type | Description | Default |
|---|---|---|---|
| generate | String | Auto-generate (multiple values comma-separated) | |
| show-toggle-icon | boolean | Show dropdown toggle icon | true |

Body: `<menu-item>` elements (each `<menu-item>` has same attributes as `<button>`).
```xml
<sub-menu label="More" icon="fa fa-ellipsis-v">
    <menu-item label="Export" icon="fa fa-download"><invoke operation-id="export"/></menu-item>
    <menu-item label="Print"><print url="/reports/print"/></menu-item>
</sub-menu>
```

## `<clipboard-button>` — Copy to Clipboard
Copies data to clipboard. Has all common button attributes plus:
| Attribute | Type | Description | Default |
|---|---|---|---|
| data | String (required) | Data to copy (supports placeholders) | |
| type | text/html | Data type | text |
| message | String | Success message | |

```xml
<clipboard-button label="Copy ID" data="{id}" message="ID copied!"/>
<clipboard-button label="Copy HTML" data="{htmlContent}" type="html"/>
```

## `<group>` — Button Group
| Attribute | Type | Description |
|---|---|---|
| generate | String | Auto-generate (crud/create/update/delete/close/submit) |
Body: `<button>`, `<sub-menu>`

## `<toolbar>` — Container
| Attribute | Type | Default |
|---|---|---|
| place | topLeft/topRight/bottomLeft/bottomRight/topCenter/bottomCenter | topLeft |
| generate | String | Auto-generate CRUD buttons |

```xml
<toolbar place="bottomRight">
    <button label="Save" color="primary"><invoke operation-id="save" close-on-success="true"/></button>
    <button label="Cancel"><close/></button>
</toolbar>

<toolbar place="topLeft" generate="crud"/><!-- Auto: Create, Edit, Delete buttons -->
```

## `<dependencies>` — Button Dependencies
Optional child element for `<button>` and `<clipboard-button>`. Defines dynamic conditions.

### `<enabling>` — Availability Condition
| Attribute | Type | Description |
|---|---|---|
| datasource | Reference | Datasource for condition evaluation |
| model | resolve/edit/filter/multi/datasource | Model for condition evaluation |
| message | String | Message explaining why button is disabled |

Body: JS expression returning boolean.

### `<visibility>` — Visibility Condition
| Attribute | Type | Description |
|---|---|---|
| datasource | Reference | Datasource for condition evaluation |
| model | resolve/edit/filter/multi/datasource | Model for condition evaluation |

Body: JS expression returning boolean.

```xml
<button label="Approve">
    <dependencies>
        <enabling datasource="main" model="resolve" message="Select a record first">id != null</enabling>
        <visibility>status == 'draft'</visibility>
    </dependencies>
    <invoke operation-id="approve"/>
</button>
```

## Auto-Generated Button Templates (`generate`)
- `crud` → Create + Update + Delete
- `create` → "Create" button with show-modal
- `update` → "Edit" button with show-modal
- `delete` → "Delete" button with invoke + confirm
- `close` → "Close" button
- `submit` → "Save" button with submit

## See Also
- `action.md` — action types placed inside buttons
- `widget.md` — widget toolbar configuration
- `cell.md` — toolbar cells in table columns
