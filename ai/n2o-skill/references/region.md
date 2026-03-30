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
| Attribute | Type | Default |
|---|---|---|
| label | String | |
| collapsible | boolean | true |
| has-separator | boolean | true |
| expand | boolean | true |

Settings: `n2o.api.region.line.collapsible=true`, `n2o.api.region.line.has_separator=true`, `n2o.api.region.line.expand=true`
```xml
<line collapsible="true" label="Details" expand="false"><form/><table/></line>
```

## `<panel>` — Panel with header
| Attribute | Type | Default |
|---|---|---|
| title | String | |
| header | boolean | true |
| collapsible | boolean | true |
| open | boolean | true |
| icon | String (FontAwesome) | |
| color | primary/secondary/success/danger/warning/info/light/dark | |
| footer-title | String | |
| routable | boolean | true |
| active-param | String | value of `id` |

Settings: `n2o.api.region.panel.collapsible=true`, `n2o.api.region.panel.open=true`, `n2o.api.region.panel.routable=true`
```xml
<panel collapsible="true" title="Info" color="primary" icon="fa fa-info-circle"><form/></panel>
```

## `<tabs>` — Tabbed container
| Attribute | Type | Default |
|---|---|---|
| always-refresh | boolean | false |
| lazy | boolean | true |
| hide-single-tab | boolean | false |
| max-height | String (px/em/rem) | |
| scrollbar | boolean | false |
| routable | boolean | true |
| active-param | String | value of `id` |
| datasource | Reference | |
| active-tab-field-id | String | |

Settings: `n2o.api.region.tabs.always_refresh=false`, `n2o.api.region.tabs.lazy=true`, `n2o.api.region.tabs.routable=true`, `n2o.api.region.tabs.scrollbar=false`

### `<tab>` Attributes
| Attribute | Type | Description |
|---|---|---|
| id | String | Tab identifier |
| name | String | Tab label |
| datasource | Reference | Tab datasource |
| visible | String | Visibility condition (supports placeholders) |
| enabled | String | Enabled condition (supports placeholders) |

```xml
<tabs always-refresh="false" lazy="true">
    <tab name="General" id="general"><form datasource="ds1"/></tab>
    <tab name="Details" id="details"><table datasource="ds2"/></tab>
    <tab name="Contacts" id="contacts" visible="type.id == 1"><form/></tab>
</tabs>
```

## `<scrollspy>` — Scroll-tracking side navigation
| Attribute | Type | Default |
|---|---|---|
| active | String | |
| title | String | |
| placement | left / right | left |
| headlines | boolean | false |
| max-height | String (px) | |
| routable | boolean | true |
| active-param | String | value of `id` |

Body: `<menu-item>`, `<sub-menu>`, and `<group>` elements, each containing widgets.

### `<group>` Attributes (scrollspy)
| Attribute | Type | Default |
|---|---|---|
| id | String | |
| title | String | |
| headline | boolean | false |

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
| Attribute | Type | Default |
|---|---|---|
| datasource | Reference | |
| direction | row / column | row |
| model | resolve / filter / multi / datasource | resolve |
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
