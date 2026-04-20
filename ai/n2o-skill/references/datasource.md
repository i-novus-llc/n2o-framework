# Datasources

Datasources connect widgets to data. Defined in `<datasources>` of a page or inline in a widget.

## `<datasource>` — Standard Datasource
| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Unique identifier (required) | |
| query-id | Reference | Query file | |
| object-id | Reference | Object file | |
| size | Number | Records per page | 10 |
| default-values-mode | query / defaults / merge | Value initialization mode | query |
| route | String | URL route for filters | |
| fetch-on-init | boolean | Fetch data on page initialization | false |

Body: `<filters>`, `<submit>`, `<dependencies>`

### `<submit>` — Auto-submit Configuration
| Attribute | Type | Description | Default |
|---|---|---|---|
| operation-id | Reference | Operation to invoke (required) | |
| route | String | URL route for submit | |
| auto-submit-on | change / blur | Auto-submit trigger event | |
| refresh-on-success | boolean | Refresh data after success | false |
| refresh-datasources | String (comma-sep) | Datasources to refresh | |
| message-on-success | boolean | Show message on success | false |
| message-on-fail | boolean | Show message on fail | false |
| message-position | fixed / relative | Message position | |
| message-placement | top / bottom / topLeft / topRight / bottomLeft / bottomRight | Message placement | |
| message-widget-id | String | Widget ID for relative message | |
| submit-all | boolean | Submit entire form or only form-param fields | true |

Body: `<path-param>`, `<header-param>`, `<form-param>`

### `<dependencies>`
```xml
<dependencies>
    <fetch on="parentDs" model="resolve"/><!-- Refetch when parent changes -->
    <copy on="sourceDs" source-field-id="name" target-field-id="title"/>
</dependencies>
```

#### `<fetch>` — Refetch on Change
| Attribute | Type | Description |
|---|---|---|
| on | String | Source datasource id (required) |
| model | resolve/edit/filter/multi/datasource | Source model |
| field-id | String | Field of the source datasource to watch |

#### `<copy>` — Copy Data from Another Datasource
| Attribute | Type | Description | Default |
|---|---|---|---|
| on | String | Source datasource id (required) | |
| source-model | resolve/edit/filter/multi/datasource | Source model | resolve |
| source-field-id | String | Source field (default: entire model) | |
| target-model | resolve/edit/filter/multi/datasource | Target model | resolve |
| target-field-id | String | Target field (default: entire model) | |
| submit | boolean | Submit after copy | false |
| apply-on-init | boolean | Copy on datasource init | false |

### `<filters>` — Preset Filters
| Element | Description |
|---|---|
| `<eq>` | Equals |
| `<notEq>` | Not equals |
| `<in>` | In list |
| `<notIn>` | Not in list |
| `<like>` | Contains |
| `<likeStart>` | Starts with |
| `<more>` | Greater than |
| `<less>` | Less than |
| `<isNull>` | Is null |
| `<isNotNull>` | Is not null |
| `<contains>` | Array contains |
| `<overlaps>` | Arrays overlap |

Filter attributes:
| Attribute | Type | Description | Default |
|---|---|---|---|
| field-id | String | Field to filter (required) | |
| value / values | String | Filter value (supports placeholders) | |
| required | boolean | Filter is required | |
| datasource | String | Datasource for filter value | |
| model | resolve/edit/filter/multi/datasource | Model for filter value | resolve |
| param | String | URL parameter name | |
| routable | boolean | Include filter in URL | false |

```xml
<datasource id="employees" query-id="employees" object-id="employee" size="10">
    <filters>
        <eq field-id="org.id" value="{orgId}"/>
        <in field-id="status" values="active,pending"/>
        <isNotNull field-id="email"/>
    </filters>
    <submit operation-id="save" auto-submit-on="change"/>
</datasource>
```

---

## `<inherited-datasource>` — Inherits from Another Datasource
| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Unique identifier (required) | |
| source-datasource | Reference | Parent datasource id (required) | |
| source-model | resolve/edit/filter/multi/datasource | Parent model | |
| source-field-id | String | Field in parent to inherit | |
| size | Number | Records per page | 10 |

Body: `<submit>`, `<fetch-value>`, `<filters>`, `<dependencies>`

### `<submit>` for inherited-datasource
| Attribute | Type | Description | Default |
|---|---|---|---|
| auto | boolean | Auto-submit on changes | true |
| model | resolve/edit/filter/multi/datasource | Source model to save | resolve |
| target-datasource | String | Target datasource | source-datasource value |
| target-model | resolve/edit/filter/multi/datasource | Target model | source-model value |
| target-field-id | String | Target field | source-field-id value |

Body: `<submit-value>` (JS expression for transforming data)

### `<fetch-value>`
JS expression for transforming data when copying to inherited-datasource (written in element body).

Used for master-detail patterns without extra server calls.
```xml
<datasource id="parentDs" query-id="orders" size="10"/>
<inherited-datasource id="orderItems" source-datasource="parentDs" source-field-id="items">
    <filters>
        <eq field-id="status" value="active"/>
    </filters>
</inherited-datasource>
```

---

## `<parent-datasource>` — Reference Parent Page Datasource
References a datasource from the parent page (for nested pages/modals).
| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Unique identifier (required) | |
| source-datasource | String | Datasource id from parent page | id value |

```xml
<parent-datasource id="parentOrder" source-datasource="order"/>
```

---

## `<app-datasource>` — Reference Application-Level Datasource
References a datasource defined in `application.xml`.
| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Unique identifier (required) | |
| source-datasource | String | Datasource id from application.xml | id value |

```xml
<app-datasource id="currentUser" source-datasource="currentUser"/>
```
Then use `datasource="currentUser"` in widgets.

---

## `<browser-storage>` — Browser Storage Datasource
| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Identifier (required) | |
| type | sessionStorage / localStorage | Storage type | sessionStorage |
| key | String | Storage key | datasource global id |
| size | Number | Records per page | 10 |
| fetch-on-init | boolean | Fetch data on page initialization | false |

Body: `<submit>`, `<dependencies>`

### `<submit>` for browser-storage
| Attribute | Type | Description | Default |
|---|---|---|---|
| auto | boolean | Auto-save on changes | true |
| type | sessionStorage / localStorage | Storage type for saving | |
| key | String | Storage key | datasource key |
| model | resolve/edit/filter/multi/datasource | Model to save | resolve |

```xml
<browser-storage id="prefs" type="localStorage" key="userPrefs">
    <submit auto="true" model="edit"/>
</browser-storage>
```

---

## `<cached-datasource>` — Cached Datasource
Combines datasource and browser-storage behavior with cache expiration.
| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Unique identifier (required) | |
| query-id | Reference | Query file | |
| object-id | Reference | Object file | |
| size | Number | Records per page | 10 |
| route | String | URL for data fetching | |
| storage-type | sessionStorage / localStorage | Browser storage type | sessionStorage |
| storage-key | String | Storage key | datasource global id |
| cache-expires | String | Cache validity period (format: "1d 3h 2m") | |
| fetch-on-init | boolean | Fetch data on page initialization | false |

Body: `<submit>`, `<filters>`, `<dependencies>`

### `<submit>` for cached-datasource
| Attribute | Type | Description | Default |
|---|---|---|---|
| operation-id | Reference | Operation to invoke (required) | |
| route | String | URL route for submit | |
| refresh-on-success | boolean | Refresh data after success | false |
| refresh-datasources | String (comma-sep) | Datasources to refresh | |
| message-on-success | boolean | Show message on success | false |
| message-on-fail | boolean | Show message on fail | false |
| message-position | fixed / relative | Message position | |
| message-placement | top / bottom / topLeft / topRight / bottomLeft / bottomRight | Message placement | |
| message-widget-id | String | Widget ID for relative message | |
| submit-all | boolean | Submit entire form or only form-param fields | true |
| clear-cache-after-submit | boolean | Clear cache after submit | false |

Body: `<path-param>`, `<header-param>`, `<form-param>`

```xml
<cached-datasource id="dictionaries" query-id="dictionaries"
                   storage-type="localStorage" cache-expires="1d">
    <filters>
        <eq field-id="active" value="true"/>
    </filters>
</cached-datasource>
```

---

## `<stomp-datasource>` — WebSocket STOMP Datasource
| Attribute | Type | Description |
|---|---|---|
| id | String | Identifier (required) |
| destination | String | STOMP destination topic (required) |

Body: `<values>` — initial values

### `<values>` — Initial Values
```xml
<stomp-datasource id="notifications" destination="/topic/notifications">
    <values>
        <value count="0" status="disconnected"/>
    </values>
</stomp-datasource>
```

---

## Models
Each datasource has multiple models:
| Model | Description |
|---|---|
| `resolve` | Selected/current record (read-only) |
| `edit` | Editable copy of current record |
| `filter` | Filter values |
| `multi` | Multiple selected records (for multi-select tables) |
| `datasource` | Full datasource state |

Widgets use `model` attribute to specify which model they read/write:
- Tables default to `resolve`
- Forms default to `edit`
- Filters default to `filter`

## See Also
- `page.md` — page-level datasource definitions
- `widget.md` — inline widget datasources
- `application.md` — app-level datasources
- `query.md` — queries referenced by datasources
- `object.md` — objects referenced by datasources
