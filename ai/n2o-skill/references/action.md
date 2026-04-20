# Actions (action-2.0)

Schema: `http://n2oapp.net/framework/config/schema/action-2.0`
Actions are placed inside `<toolbar>` buttons or defined in `<actions>` at page/widget level and referenced via `action-id`.

## Navigation Actions

### `<a>` — Open URL link
| Attribute | Type | Description | Default |
|---|---|---|---|
| href | String | URL address (required). Supports placeholders | |
| datasource | Reference | Datasource for placeholder values | parent's datasource |
| model | resolve/edit/filter/multi/datasource | Model for placeholder values | parent's model |
| target | newWindow / self / application | Link open scenario | self |

Body: `<path-param>`, `<query-param>`
```xml
<a href="/api/document/{id}" target="newWindow">
    <path-param name="id" value="{id}"/>
</a>
```

### `<open-page>` — Navigate to a page
| Attribute | Type | Description | Default |
|---|---|---|---|
| page-id | Reference | Target page file id (required) | |
| page-name | String | Page name | |
| route | String | URL route | auto-generated |
| target | newWindow / application | Open scenario | application |
| refresh-on-close | boolean | Refresh parent datasource on close | false |
| refresh-datasources | String (comma-sep) | Datasources to refresh | parent's datasource |
| unsaved-data-prompt-on-close | boolean | Warn on unsaved data when closing | false |

Body: `<params>`, `<datasources>`, `<breadcrumbs>`, `<toolbars>`, `<actions>`
```xml
<open-page page-id="personCard" route="/edit">
    <params><path-param name="id" value="{id}"/></params>
</open-page>
```

### `<show-modal>` — Open page in modal dialog
Same attributes as `<open-page>` plus:
| Attribute | Type | Description | Default |
|---|---|---|---|
| modal-size | sm / md / lg / xl | Modal window size | lg |
| backdrop | true / false / static | Background behavior (true=closes on click, false=transparent, static=no close on click) | static |
| scrollable | boolean | Fixed size with scrollbar inside | false |
| has-header | boolean | Show header | true |
| class | String | CSS class | |
| style | String | CSS style | |

```xml
<show-modal page-id="personCard" modal-size="lg">
    <params><path-param name="id" value="{id}"/></params>
</show-modal>
```

### `<open-drawer>` — Open page in slide-out drawer
Same as `<open-page>` plus:
| Attribute | Type | Description | Default |
|---|---|---|---|
| width | String (px/%) | Drawer width | 400 |
| height | String (px/%) | Drawer height | |
| placement | left / right / top / bottom | Position | right |
| closable | boolean | Show close button | true |
| backdrop | boolean | Show background | true |
| close-on-escape | boolean | Close on Esc key | true |
| close-on-backdrop | boolean | Close on backdrop click | true |
| fixed-footer | boolean | Fix footer position | false |

### `<close>` — Close modal/drawer/page
| Attribute | Type | Description               | Default |
|---|---|---------------------------|---|
| unsaved-data-prompt | boolean | Show unsaved data warning | true |
| refresh | boolean | Refresh parent datasource | false |
| target | page/tab | Target of close | page |
```xml
<close/>
```

---

## Data Actions

### `<invoke>` — Call object operation
| Attribute | Type | Description | Default |
|---|---|---|---|
| operation-id | Reference | Operation id in object (required) | |
| object-id | Reference | Object containing the operation | |
| route | String | Operation URL | |
| method | POST / PUT / DELETE | HTTP method | POST |
| close-on-success | boolean | Close after success | false |
| double-close-on-success | boolean | Close 2 levels | false |
| close-on-fail | boolean | Close after failure | false |
| refresh-on-success | boolean | Refresh datasource | true |
| refresh-datasources | String (comma-sep) | Extra datasources to refresh | parent's datasource |
| redirect-url | String | Redirect URL after success | |
| redirect-target | newWindow / self / application | Redirect target | self |
| clear-on-success | boolean | Clear after success | false |
| optimistic | boolean | Optimistic UI update | false |
| submit-all | boolean | Submit all form fields | true |
| message-on-success | boolean | Show success message | true |
| message-on-fail | boolean | Show failure message | true |
| message-position | fixed / relative | Message position | fixed |
| message-placement | top / bottom / topLeft / topRight / bottomLeft / bottomRight | Message placement | top |
| use-fail-out | boolean | Use fail-out parameters on failure | true |

Body: `<path-param>`, `<form-param>`, `<header-param>`
```xml
<invoke operation-id="delete" close-on-success="true">
    <path-param name="id" value="{id}"/>
    <form-param id="status" value="{status.id}"/>
</invoke>
```

### `<action>` — Custom Redux action
| Attribute | Type | Description | Default |
|---|---|---|---|
| type | String | Redux action type (required) | |
| close-on-success | boolean | Close after success | false |
| double-close-on-success | boolean | Close 2 levels | false |
| close-on-fail | boolean | Close after failure | false |
| refresh-on-success | boolean | Refresh datasource | true |
| refresh-datasources | String (comma-sep) | Datasources to refresh | parent's datasource |
| redirect-url | String | Redirect URL after success | |
| redirect-target | newWindow / self / application | Redirect target | self |

Body: `<payload>` with any attributes (supports placeholders)
```xml
<action type="MY_CUSTOM_ACTION">
    <payload customField="{fieldValue}"/>
</action>
```

### `<submit>` — Submit datasource
| Attribute | Type | Description | Default |
|---|---|---|---|
| datasource | Reference | Datasource to submit | parent's datasource |
```xml
<submit datasource="ds1"/>
```

---

## UI Actions

### `<refresh>` — Refresh datasource(s)
| Attribute | Type | Description | Default |
|---|---|---|---|
| datasource | Reference | Datasource to refresh | parent's datasource |
```xml
<refresh datasource="ds1"/>
```

### `<clear>` — Clear datasource
| Attribute | Type | Description | Default |
|---|---|---|---|
| datasource | Reference | Datasource to clear | parent's datasource |
| model | String (comma-sep) | Models to clear | parent's model |
| close-on-success | boolean | Close window after clear | false |

### `<copy>` — Copy data between datasources
| Attribute | Type | Description | Default |
|---|---|---|---|
| source-datasource | Reference | Source datasource | parent's datasource |
| source-model | resolve/edit/filter/multi/datasource | Source model | resolve |
| source-field-id | String | Source field (omit for whole model) | |
| target-datasource | Reference | Target datasource | source-datasource |
| target-model | resolve/edit/filter/multi/datasource | Target model | resolve |
| target-field-id | String | Target field (omit for whole model) | |
| mode | merge / replace / add | Merge mode | merge |
| close-on-success | boolean | Close window after copy | false |
| validate | boolean | Trigger field validation | true |

### `<set-value>` — Set field value via JS expression
| Attribute | Type | Description | Default |
|---|---|---|---|
| source-datasource | Reference | Datasource for expression context | parent's datasource |
| source-model | resolve/edit/filter/multi/datasource | Model for expression context | parent's model |
| target-datasource | Reference | Target datasource | parent's datasource |
| target-model | resolve/edit/filter/multi/datasource | Target model | parent's model |
| to | String | Target field (omit for whole model) | |
| merge-mode | merge / replace / add | Merge mode | replace |
| validate | boolean | Trigger field validation | true |

Body: JS expression (fields referenced by name)
```xml
<set-value to="total" target-datasource="ds1">price * quantity</set-value>
```

### `<alert>` — Show notification
| Attribute | Type | Description | Default |
|---|---|---|---|
| title | String | Notification title (supports placeholders) | |
| text | String | Notification text (supports placeholders) | |
| color | primary/secondary/success/danger/warning/info | Color | secondary |
| placement | top / bottom / topLeft / topRight / bottomLeft / bottomRight | Position | top |
| href | String | URL to navigate on click | |
| time | String | Notification send time | |
| timeout | String (ms) | Display duration | 3000 |
| close-button | boolean | Show close button | true |
| datasource | Reference | Datasource for placeholders | parent's datasource |
| model | resolve/edit/filter/multi/datasource | Model for placeholders | parent's model |
| class | String | CSS class | |
| style | String | CSS style | |
```xml
<alert text="Saved!" color="success" placement="topRight"/>
```

### `<confirm>` — Show confirmation dialog
| Attribute | Type | Description | Default |
|---|---|---|---|
| title | String | Dialog title (supports placeholders) | |
| text | String | Confirmation text (supports placeholders) | |
| type | popover / modal | Dialog type | modal |
| close-button | boolean | Show close button | false |
| class | String | CSS class | |
| style | String | CSS style | |

Body: `<ok>` and `<cancel>` buttons with attributes:
| Attribute | Type | Description |
|---|---|---|
| label | String | Button label |
| color | String | Button color |
| icon | String | FontAwesome icon |
| class | String | CSS class |
| style | String | CSS style |

```xml
<confirm title="Warning" text="Are you sure?">
    <ok label="Yes" color="danger"/>
    <cancel label="No"/>
</confirm>
```

### `<validate>` — Validate fields
| Attribute | Type | Description | Default |
|---|---|---|---|
| datasource | Reference | Datasource to validate | parent's datasource |
| model | resolve / filter / edit | Model to validate | parent's model |
| break-on | danger / warning / false | When to break action chain | danger |

Body: `<field id="..."/>` elements to validate specific fields only
```xml
<validate datasource="ds1" model="edit" break-on="danger">
    <field id="name"/>
    <field id="email"/>
</validate>
```

### `<print>` — Print document
| Attribute | Type | Description | Default |
|---|---|---|---|
| url | String | Document URL (required, supports placeholders) | |
| type | pdf / text / image | Document type | pdf |
| keep-indent | boolean | Preserve indentation (for text type) | false |
| document-title | String | Document title | |
| loader | boolean | Show loader modal | false |
| loader-text | String | Loader modal text | |
| base64 | boolean | Print from base64 | false |

Body: `<path-param>`, `<query-param>`
```xml
<print url="/api/report/{id}" type="pdf">
    <path-param name="id" value="{id}"/>
</print>
```

### `<edit-list>` — Inline list editing
| Attribute | Type | Description | Default |
|---|---|---|---|
| operation | create / createMany / update / delete / deleteMany | Operation type (required) | |
| primary-key | String | Key field for update/delete | id |
| item-datasource | Reference | Datasource for item data | parent's datasource |
| item-model | resolve/edit/filter/multi/datasource | Model for item data | parent's model |
| item-field-id | String | Field for item (omit for whole model) | |
| datasource | Reference | List datasource to update | item-datasource |
| model | resolve/edit/filter/multi/datasource | List model to update | item-model |
| list-field-id | String | List field to update (omit for whole model) | |
```xml
<edit-list operation="create" list-field-id="contacts" primary-key="id"/>
```

---

## Conditional / Composite Actions

### `<if-else>` — Conditional action

`<if>` attributes:
| Attribute | Type | Description | Default |
|---|---|---|---|
| test | String | JS condition expression (required) | |
| datasource | Reference | Datasource for condition context | parent's datasource |
| model | resolve/edit/filter/multi/datasource | Model for condition context | parent's model |

`<else-if>` attributes:
| Attribute | Type | Description | Default |
|---|---|---|---|
| test | String | JS condition expression (required) | |

```xml
<if test="status.id == 1" datasource="ds1" model="resolve">
    <invoke operation-id="approve"/>
</if>
<else-if test="status.id == 2">
    <invoke operation-id="reject"/>
</else-if>
<else>
    <alert text="Unknown status" color="warning"/>
</else>
```

### `<switch>` — Switch on field
| Attribute | Type | Description | Default |
|---|---|---|---|
| value-field-id | String | Field to compare (required) | |
| datasource | Reference | Datasource for field value | parent's datasource |
| model | resolve/edit/filter/multi/datasource | Model for field value | parent's model |

```xml
<switch value-field-id="type.id" datasource="ds1" model="resolve">
    <case value="1"><open-page page-id="typeOnePage"/></case>
    <case value="2"><open-page page-id="typeTwoPage"/></case>
    <default><alert text="Unknown type"/></default>
</switch>
```

### `<on-fail>` — Handle operation errors
Standalone action to handle failures (used separately, not nested in invoke).
| Body elements |
|---|
| `<close>`, `<clear>`, `<refresh>`, `<alert>`, `<set-value>` |
```xml
<on-fail>
    <alert text="Save failed!" color="danger"/>
</on-fail>
```

---

## `<params>` — Passing Parameters
```xml
<params>
    <path-param name="id" value="{id}"/>         <!-- In URL path -->
    <query-param name="filter" value="{type}"/>   <!-- As query string -->
</params>
```

### Parameter types
| Element | Description |
|---|---|
| `<path-param>` | URL path parameter |
| `<query-param>` | Query string parameter |
| `<form-param>` | Request body parameter (uses `id` instead of `name`) |
| `<header-param>` | Request header parameter |

Common attributes for all param types:
| Attribute | Type | Description | Default |
|---|---|---|---|
| name / id | String | Parameter name (required) | |
| value | String | Value (supports placeholders like `{fieldId}`) | |
| datasource | Reference | Datasource for placeholder values | parent's datasource |
| model | resolve/edit/filter/multi/datasource | Model for placeholder values | parent's model |

## Page Override Elements

Inside `<open-page>`, `<show-modal>`, `<open-drawer>`:
```xml
<open-page page-id="personCard">
    <datasources>
        <!-- Override datasources in opened page -->
    </datasources>
    <breadcrumbs>
        <!-- Override breadcrumbs -->
    </breadcrumbs>
    <toolbars>
        <toolbar place="bottomRight">
            <!-- Override toolbar buttons -->
        </toolbar>
    </toolbars>
    <actions>
        <action id="save">
            <!-- Override action with id="save" -->
            <invoke operation-id="customSave"/>
        </action>
    </actions>
</open-page>
```

## See Also
- `button.md` — buttons that trigger actions
- `object.md` — operations called by invoke
- `page.md` — pages opened by open-page/show-modal
- `datasource.md` — datasources refreshed/cleared by actions
