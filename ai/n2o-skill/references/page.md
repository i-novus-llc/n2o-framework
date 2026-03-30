# Pages (page-4.0)

Schema: `http://n2oapp.net/framework/config/schema/page-4.0`
Files: `[id].page.xml`

## Page Types

### `<simple-page>` — Single widget page
```xml
<simple-page xmlns="http://n2oapp.net/framework/config/schema/page-4.0" name="My Page">
    <form>...</form><!-- Exactly one widget -->
</simple-page>
```

### `<page>` — Complex page with regions, datasources, actions
```xml
<page xmlns="http://n2oapp.net/framework/config/schema/page-4.0" name="Complex Page">
    <datasources>...</datasources>
    <regions>...</regions>
    <toolbar>...</toolbar>
    <actions>...</actions>
    <events>...</events>
</page>
```

### `<search-page>` — Page with built-in search
### `<left-right-page>` — Two-column layout (left + right regions)
### `<top-left-right-page>` — Three-region layout (top + left + right)

## Base Page Attributes
| Attribute | Type | Description | Default |
|---|---|---|---|
| name | String | Page title (breadcrumbs) | |
| title | String | Page title (displayed on page) | |
| html-title | String | Browser tab title | name |
| route | String | URL route | |
| datasource | String | Page datasource id | |
| model | filter / resolve / edit / datasource / multi | Page data model | resolve |
| src | String | React component | |
| class | String | CSS class | |
| style | String | CSS style | |
| show-title | boolean | Show page title | false |

## Page Body Elements
| Element | Description |
|---|---|
| `<datasources>` | Data source definitions |
| `<regions>` | Layout regions containing widgets |
| `<toolbar>` | Page-level toolbar buttons |
| `<actions>` | Named reusable actions (referenced via action-id) |
| `<events>` | Data change event handlers |
| `<breadcrumbs>` | Breadcrumb overrides |

## `<datasource>` Inside Page
| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Datasource identifier (required) | |
| query-id | Reference | Query file id | |
| object-id | Reference | Object file id | |
| size | Number | Records per page | 10 |
| default-values-mode | query / defaults / merge | How initial values are loaded | query |
| route | String | URL route for filters | |
| fetch-on-init | boolean | Fetch data on page init | false |

**Body**: `<dependencies>`, `<filters>`, `<submit>`

```xml
<datasources>
    <datasource id="ds1" query-id="employees" object-id="employee" size="10">
        <dependencies>
            <fetch on="parentDs" model="resolve"/>
        </dependencies>
        <filters>
            <eq field-id="org.id" value="{orgId}"/>
            <eq field-id="id" param="person_id"/>
        </filters>
        <submit operation-id="save" refresh-on-success="true"/>
    </datasource>
</datasources>
```

## `<dependencies>` — Datasource dependencies

### `<fetch>` — Refresh on dependency change
| Attribute | Type | Description |
|---|---|---|
| on | String | Dependent datasource id (required) |
| model | filter / resolve / edit / datasource / multi | Model to watch |

### `<copy>` — Copy data from another datasource
| Attribute | Type | Description | Default |
|---|---|---|---|
| on | String | Source datasource id (required) | |
| source-model | clientModel | Source model | resolve |
| source-field-id | String | Source field (omit for whole model) | |
| target-model | clientModel | Target model | resolve |
| target-field-id | String | Target field (omit for whole model) | |
| submit | boolean | Submit after copy | false |
| apply-on-init | boolean | Apply on datasource init | false |

## Filter Types (inside `<filters>`)
| Element | Description | Data Type |
|---|---|---|
| `<eq>` / `<notEq>` | Equals / not equals | Any |
| `<like>` / `<likeStart>` | Contains / starts with | Strings |
| `<in>` / `<notIn>` | In list / not in list | Simple types |
| `<isNull>` / `<isNotNull>` | Null check | Any |
| `<more>` / `<less>` | Greater / less than | Numbers, dates |
| `<contains>` / `<overlaps>` | Array contains / overlap | List types |

### Filter Attributes
| Attribute | Type | Description |
|---|---|---|
| field-id | String | Query field reference (required) |
| value | String | Static value or `{fieldRef}` from datasource |
| param | String | URL parameter name |
| datasource | String | Source datasource id |
| model | filter / resolve / edit / datasource / multi | Source model | resolve |
| routable | boolean | Include filter in URL | false |
| required | boolean | Filter is required | |

```xml
<filters>
    <eq field-id="id" param="person_id"/>              <!-- From URL -->
    <eq field-id="org.id" value="{orgId}"/>            <!-- From datasource field -->
    <like field-id="name" value="Test" routable="true"/><!-- Static with URL sync -->
</filters>
```

## `<events>` — React to data changes

### `<on-change>` Attributes
| Attribute | Type | Description | Default |
|---|---|---|---|
| datasource | String | Datasource to watch (required) | |
| model | filter / resolve / edit / datasource / multi | Model to watch | resolve |
| field-id | String | Field to watch (omit for whole model) | |
| action-id | String | Page action to invoke on change | |

```xml
<events>
    <on-change datasource="ds1" model="resolve" field-id="type">
        <set-value to="category" target-datasource="ds1">
            return type.name;
        </set-value>
    </on-change>
    <on-change datasource="ds1" action-id="refresh"/>
</events>
```

## `<actions>` — Named reusable actions
```xml
<actions>
    <action id="create"><show-modal page-id="createCard"/></action>
    <action id="edit">
        <show-modal page-id="editCard">
            <params><path-param name="id" value="{id}"/></params>
        </show-modal>
    </action>
</actions>
```
Referenced from buttons via `action-id="create"`.

## `<breadcrumbs>` — Custom breadcrumbs

### `<crumb>` Attributes
| Attribute | Type | Description |
|---|---|---|
| label | String | Breadcrumb text (required), supports placeholders |
| path | String | Navigation path |

```xml
<breadcrumbs>
    <crumb label="Home" path="/"/>
    <crumb label="{name}"/>
</breadcrumbs>
```

## See Also
- `region.md` — layout regions inside pages
- `widget.md` — widgets placed in regions
- `datasource.md` — datasource types and configuration
- `action.md` — named actions referenced by action-id
- `snippets.md` — ready-to-use page templates
