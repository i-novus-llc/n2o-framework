# Application Structure (application-3.0)

Schema: `http://n2oapp.net/framework/config/schema/application-3.0`
File: `application.xml`

## `<application>` Root Element
| Attribute | Type | Description | Default |
|---|---|---|---|
| welcome-page-id | String | ID of the welcome/home page | |
| navigation-layout | fullSizeHeader / fullSizeSidebar | Layout mode for header and sidebar | fullSizeHeader |
| navigation-layout-fixed | boolean | Fix header and sidebar on scroll | false |

## `<header>` — Page header
| Attribute | Type | Description |
|---|---|---|
| title | String | Application title (supports placeholders) |
| datasource | String | Datasource ID for placeholders |
| logo-src | String | Logo image URL |
| home-page-url | String | URL when clicking logo |
| sidebar-icon | String | Icon to open sidebar (FontAwesome) |
| sidebar-toggled-icon | String | Icon to hide sidebar (defaults to sidebar-icon) |
| src | String | React component |
| class | String | CSS class |
| style | String | CSS style |

**Body**: `<nav>` (main menu), `<extra-menu>` (e.g. user menu), `<search>` (search panel). Nav and extra-menu support `ref-id` to reference external `*.menu.xml` files.

### `<search>` — Search panel in header
| Attribute | Type | Description | Default |
|---|---|---|---|
| query-id | String | **required** — Query ID for search data | |
| filter-field-id | String | Query field for filtering | |
| url-field-id | String | Query field for result URL | |
| label-field-id | String | Query field for result label | |
| icon-field-id | String | Query field for result icon | |
| description-field-id | String | Query field for result description | |
| target | application / newWindow / self | How to open search result | application |

## `<sidebars>` — Multiple sidebars with URL-based switching
Contains `<sidebar>` elements. The sidebar whose `path` matches current page URL is displayed.

### `<sidebar>` Attributes
| Attribute | Type | Description | Default |
|---|---|---|---|
| title | String | Sidebar title (supports placeholders) | |
| subtitle | String | Subtitle (supports placeholders) | |
| path | String | URL pattern match | |
| ref-id | Reference | External sidebar file | |
| datasource | String | Datasource ID for placeholders | |
| logo-src | String | Logo image | |
| logo-class | String | CSS class for logo | |
| home-page-url | String | Logo click URL | |
| side | left / right | Side position | left |
| default-state | none / micro / mini / maxi | Default state | * |
| toggled-state | none / micro / mini / maxi | State after toggle | * |
| toggle-on-hover | boolean | Toggle on mouse hover | false |
| overlay | boolean | Overlay mode | false |
| src | String | React component | |
| class | String | CSS class | |
| style | String | CSS style | |

\* `default-state` defaults to `none` if header has `sidebar-icon`/`sidebar-toggled-icon`, otherwise `maxi`. `toggled-state` defaults to `none` if header has sidebar icons, `mini` if `default-state=maxi`, otherwise `maxi`.

**Path matching rules**:
- Exact: `path="/persons/list"` — only `/persons/list`
- Single wildcard: `path="/persons/*"` — `/persons/123` but NOT `/persons/123/profile`
- Multi wildcard: `path="/persons/**"` — `/persons/123/profile/edit`
- No path = default sidebar (shown when no other matches)

**Body**: `<datasource>` (inline datasource), `<nav>`, `<extra-menu>`

## `<events>` — Application-level events
Contains `<stomp-event>` elements for WebSocket/STOMP event handling at application level.

## `<footer>` Attributes
| Attribute | Type | Description |
|---|---|---|
| left-text | String | Left text (supports `${property}` placeholders) |
| right-text | String | Right text (supports placeholders) |
| src | String | React component |
| class | String | CSS class |
| style | String | CSS style |

## Application-level `<datasources>`
Datasources defined here persist across all pages. Pages reference them via `<app-datasource>`.
```xml
<application xmlns="http://n2oapp.net/framework/config/schema/application-3.0">
    <datasources>
        <datasource id="appDs" query-id="currentUser"/>
    </datasources>
</application>
```
In page:
```xml
<datasources>
    <app-datasource id="appDs"/>
</datasources>
<regions>
    <form datasource="appDs"/>
</regions>
```

## Full Example
```xml
<?xml version='1.0' encoding='UTF-8'?>
<application xmlns="http://n2oapp.net/framework/config/schema/application-3.0"
             welcome-page-id="index">
    <datasources>
        <datasource id="appDs" query-id="currentUser"/>
    </datasources>
    <header title="My App" logo-src="/img/logo.png">
        <nav ref-id="mainMenu"/>
        <extra-menu ref-id="userMenu"/>
    </header>
    <sidebars>
        <sidebar path="/home" title="Home">
            <nav ref-id="homeSidebar"/>
        </sidebar>
        <sidebar path="/admin/**" title="Admin">
            <nav ref-id="adminSidebar"/>
        </sidebar>
        <sidebar title="Default Sidebar">
            <nav ref-id="mainMenu"/>
        </sidebar>
    </sidebars>
    <footer left-text="N2O ${n2o.version}" right-text="© 2024"/>
</application>
```

## See Also
- `page.md` — pages referenced by welcome-page-id
- `datasource.md` — app-level datasources
- `settings.md` — application.properties defaults
- `access.md` — access control schemas
