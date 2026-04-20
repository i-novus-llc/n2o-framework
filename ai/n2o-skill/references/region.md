# Regions (region-3.0)

Schema: `http://n2oapp.net/framework/config/schema/region-3.0`
Regions organize widgets inside the `<regions>` element of a page.

## Base Region Attributes (inherited by all)
| Attribute | Type | Description |
|---|---|---|
| id | String | Region identifier |
| src | String | React component |
| class | String | CSS class |
| style | String | CSS style |

## `<region>` — Simple container
No extra attributes. Just groups widgets.
```xml
<region><table/><tabs/><form/></region>
```

## `<line>` — Collapsible section with separator
| Attribute | Type | Description | Default |
|---|---|---|---|
| label | String | Region heading | |
| collapsible | boolean | Whether the region can be expanded and collapsed | true |
| has-separator | boolean | Display of a horizontal separator | true |
| expand | boolean | Initial expanded state | true |

Settings: `n2o.api.region.line.collapsible=true`, `n2o.api.region.line.has_separator=true`, `n2o.api.region.line.expand=true`
```xml
<line collapsible="true" label="Details" expand="false"><form/><table/></line>
```

## `<panel>` — Panel with header
| Attribute | Type | Description | Default |
|---|---|---|---|
| title | String | Panel heading | |
| header | boolean | Whether the header is shown | true |
| collapsible | boolean | Whether the region can be expanded and collapsed | true |
| open | boolean | Initial expanded state of the panel | true |
| icon | String (FontAwesome) | Icon class | |
| color | primary/secondary/success/danger/warning/info/light/dark | Panel color | |
| footer-title | String | Title for the region footer | |
| routable | boolean | Restore active panels from URL | true |
| active-param | String | URL parameter name for the active panel | value of `id` |

Settings: `n2o.api.region.panel.collapsible=true`, `n2o.api.region.panel.open=true`, `n2o.api.region.panel.routable=true`
```xml
<panel collapsible="true" title="Info" color="primary" icon="fa fa-info-circle"><form/></panel>
```

## `<tabs>` — Tabbed container
| Attribute | Type | Description | Default |
|---|---|---|---|
| always-refresh | boolean | Refresh data when switching between tabs | false |
| lazy | boolean | Lazy loading of tabs | true |
| hide-single-tab | boolean | Hide the only available tab and show only its content | false |
| max-height | String (px/em/rem) | Maximum height of tab content | |
| scrollbar | boolean | Show scrollbar | false |
| routable | boolean | Restore active tabs from URL | true |
| active-param | String | URL parameter name for the active tab | value of `id` |
| datasource | Reference | Datasource identifier for the value of `active-tab-field-id` | |
| active-tab-field-id | String | Field identifier whose value sets the active tab id | |

Settings: `n2o.api.region.tabs.always_refresh=false`, `n2o.api.region.tabs.lazy=true`, `n2o.api.region.tabs.routable=true`, `n2o.api.region.tabs.scrollbar=false`

### `<tab>` Attributes
| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Tab identifier | |
| name | String | Tab label | |
| datasource | Reference | Tab datasource | |
| visible | String | Visibility condition (supports placeholders) | true |
| enabled | String | Enabled condition (supports placeholders) | true |

```xml
<tabs always-refresh="false" lazy="true">
    <tab name="General" id="general"><form datasource="ds1"/></tab>
    <tab name="Details" id="details"><table datasource="ds2"/></tab>
    <tab name="Contacts" id="contacts" visible="type.id == 1"><form/></tab>
</tabs>
```

## `<scrollspy>` — Scroll-tracking side navigation
| Attribute | Type | Description | Default |
|---|---|---|---|
| active | String | Default active item | |
| title | String | Menu heading | |
| placement | left / right | Side on which the menu is placed | left |
| headlines | boolean | Whether to show a separator line between blocks | false |
| max-height | String (px) | Maximum height of region content | |
| routable | boolean | Restore active items from URL | true |
| active-param | String | URL parameter name for the active menu item | value of `id` |

Body: `<menu-item>`, `<sub-menu>`, and `<group>` elements, each containing widgets.

### `<group>` Attributes (scrollspy)
| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Group identifier | |
| title | String | Group heading | |
| headline | boolean | Whether to show a separator line | false |

```xml
<scrollspy title="Navigation" placement="left">
    <menu-item id="s1" title="Section 1"><form/></menu-item>
    <sub-menu id="s2" title="Section 2">
        <menu-item id="s2a" title="Sub A"><table/></menu-item>
    </sub-menu>
</scrollspy>
```

## `<sub-page>` — Load sub-pages by URL
| Attribute | Type | Description |
|---|---|---|
| default-page-id | String | Default sub-page (defaults to first page) |

### `<page>` Attributes (sub-page)
| Attribute | Type | Description |
|---|---|---|
| page-id | String | Sub-page identifier (required) |
| route | String | URL route for sub-page (required) |

Body: `<datasources>`, `<breadcrumbs>`, `<toolbars>`, `<actions>` to override sub-page elements.
```xml
<sub-page default-page-id="profile">
    <page page-id="profile" route="/profile"/>
    <page page-id="documents" route="/documents"/>
    <page page-id="orders" route="/orders">
        <datasources><datasource id="orderDs" query-id="orders"/></datasources>
    </page>
</sub-page>
```

## `<nav>` — Navigation menu region
| Attribute | Type | Description | Default |
|---|---|---|---|
| datasource | Reference | Datasource identifier | |
| direction | row / column | Display direction of elements | row |
| model | resolve / filter / multi / datasource | Data model | resolve |
Body: `<menu-item>`, `<dropdown-menu>`, `<link>`, `<divider>`, `<button>`, `<group>`

## `<row>` — Bootstrap grid row
| Attribute | Type | Default |
|---|---|---|
| columns | Number | 12 |
| wrap | boolean | true |
| align | top/middle/bottom/stretch | top |
| justify | start/end/center/space-around/space-between/space-evenly | start |

Body: `<col>` elements with `size` (1-12), `offset`, `class`, `style`
```xml
<row columns="12">
    <col size="8"><form/></col>
    <col size="4"><table/></col>
</row>
```

## `<flex-row>` — Flexible row (no grid)
| Attribute | Type | Default |
|---|---|---|
| wrap | boolean | true |
| align | top/middle/bottom/stretch | top |
| justify | start/end/center/space-around/space-between/space-evenly | start |

## See Also
- `page.md` — pages that contain regions
- `widget.md` — widgets placed inside regions
- `fieldset.md` — row/col layout inside forms
