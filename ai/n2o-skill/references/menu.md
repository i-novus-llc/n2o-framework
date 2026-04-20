# Menu (menu-3.0)

Schema: `http://n2oapp.net/framework/config/schema/menu-3.0`
File pattern: `[id].menu.xml`

Menu files define navigation structures referenced from `application.xml` via `ref-id`. The root element is either `<nav>` (main navigation) or `<extra-menu>` (secondary navigation, e.g. user menu).

## Root Elements

Both `<nav>` and `<extra-menu>` have the same structure:

| Attribute | Type | Description |
|---|---|---|
| ref-id | Reference | Reference to parent menu file (for inheritance/override) |
| src | String | Custom React component |

**Body**: `<menu-item>`, `<button>`, `<divider>`, `<link>`, `<group>`, `<dropdown-menu>`

```xml
<!-- myMenu.menu.xml -->
<nav xmlns="http://n2oapp.net/framework/config/schema/menu-3.0">
    <menu-item name="Home">
        <open-page page-id="index"/>
    </menu-item>
    <menu-item name="Employees">
        <open-page page-id="employees"/>
    </menu-item>
</nav>
```

Reference from `application.xml`:
```xml
<header title="My App">
    <nav ref-id="mainMenu"/>
    <extra-menu ref-id="userMenu"/>
</header>
```

Inline (without separate file):
```xml
<header title="My App">
    <nav>
        <menu-item name="Home"><open-page page-id="index"/></menu-item>
    </nav>
</header>
```

---

## Common Attributes (most menu elements share these)
| Attribute | Type | Default | Description |
|---|---|---|---|
| label / name | String | | Element label (supports placeholders) |
| icon | String | | FontAwesome icon (https://fontawesome.com/v6/icons/) |
| icon-position | left / right | left | Icon position relative to label |
| visible | boolean / String | true | Visibility condition (supports placeholders) |
| enabled | boolean / String | true | Availability condition (supports placeholders) |
| datasource | Reference | | Datasource ID for placeholder resolution |
| model | resolve/edit/filter/multi/datasource | | Model for placeholder resolution |
| src | String | | Custom React component |
| class | String | | CSS class |
| style | String | | CSS style |
| action-id | Reference | | Named action to invoke on click |

---

## `<menu-item>` — Navigation Item
Standard clickable menu item. Triggers an action (typically `<open-page>`).

All common attributes plus:
| Attribute | Type | Default | Description |
|---|---|---|---|
| id | String | | Element identifier |
| image | String | | Image URL (supports placeholders) |
| image-shape | square / circle / rounded | square | Image shape |
| badge | String | | Badge text (supports placeholders) |
| badge-color | primary/secondary/success/danger/warning/info/light/dark | | Badge color (supports placeholders) |
| badge-position | left / right | right | Badge position |
| badge-shape | square / circle / rounded | circle | Badge shape |
| badge-image | String | | Badge image URL (supports placeholders) |
| badge-image-position | left / right | left | Badge image position |
| badge-image-shape | square / circle / rounded | circle | Badge image shape |

Body: An action element (`<open-page>`, `<invoke>`, `<show-modal>`, `<a>`, etc.)

```xml
<menu-item id="employees" name="Employees" icon="fa fa-users">
    <open-page page-id="employees"/>
</menu-item>

<!-- With badge (e.g. notification count) -->
<menu-item name="Messages" badge="{count}" badge-color="danger">
    <open-page page-id="messages"/>
</menu-item>

<!-- Conditional visibility -->
<menu-item name="Admin" visible="{hasRole('ADMIN')}">
    <open-page page-id="admin"/>
</menu-item>
```

---

## `<button>` — Action Button in Menu
A button that triggers an action. Like `<menu-item>` but styled as a button.

All common attributes plus:
| Attribute | Type | Default | Description |
|---|---|---|---|
| id | String | | Element identifier |
| color | primary/secondary/success/danger/warning/info/link/light/dark | | Button color (supports placeholders) |
| description | String | | Tooltip text on hover |
| tooltip-position | top / bottom / left / right | bottom | Tooltip position |
| image | String | | Image URL (supports placeholders) |
| image-shape | square / circle / rounded | square | Image shape |
| badge | String | | Badge text (supports placeholders) |
| badge-color | primary/secondary/success/danger/warning/info/light/dark | | Badge color (supports placeholders) |
| badge-position | left / right | right | Badge position |
| badge-shape | square / circle / rounded | circle | Badge shape |
| badge-image | String | | Badge image URL |
| badge-image-position | left / right | left | Badge image position |
| badge-image-shape | square / circle / rounded | circle | Badge image shape |

Body: An action element.

```xml
<button label="Logout" icon="fa fa-sign-out" color="danger">
    <invoke operation-id="logout"/>
</button>
```

---

## `<link>` — External Link
A hyperlink to an external URL.

| Attribute | Type | Default | Description |
|---|---|---|---|
| label | String | | Link text (supports placeholders) |
| icon | String | | FontAwesome icon |
| icon-position | left / right | left | Icon position |
| target | application / newWindow / self | newWindow | How to open the link |
| visible | boolean / String | true | Visibility condition |
| enabled | boolean / String | true | Availability condition |
| class | String | | CSS class |
| style | String | | CSS style |

Body: `<a href="..."/>` action element.

```xml
<link label="Documentation" icon="fa fa-book" target="newWindow">
    <a href="https://n2o.i-novus.ru/docs/"/>
</link>
```

---

## `<divider>` — Visual Separator
A horizontal rule to separate menu sections.

| Attribute | Type | Description |
|---|---|---|
| class | String | CSS class |
| style | String | CSS style |

```xml
<divider/>
```

---

## `<group>` — Collapsible Group
Groups menu items under a collapsible header.

All common attributes plus:
| Attribute | Type | Default | Description |
|---|---|---|---|
| label | String | | Group header label (supports placeholders) |
| collapsible | boolean | true | Allow expanding/collapsing |
| default-state | expanded / collapsed | expanded | Initial state |
| icon | String | | Group header icon |
| icon-position | left / right | left | Icon position |

Body: `<menu-item>`, `<button>`, `<divider>`, `<link>`, `<group>`, `<dropdown-menu>`

```xml
<group label="References" collapsible="true" default-state="collapsed">
    <menu-item name="Departments"><open-page page-id="departments"/></menu-item>
    <menu-item name="Positions"><open-page page-id="positions"/></menu-item>
</group>
```

---

## `<dropdown-menu>` — Dropdown Menu
A nested menu that opens on click (or hover).

| Attribute | Type | Default | Description |
|---|---|---|---|
| label / name | String | | Dropdown trigger label (supports placeholders) |
| trigger | click / hover | click | How to open the dropdown |
| position | auto / top / bottom / left / right | auto | Dropdown position relative to trigger |
| icon | String | | Trigger icon |
| icon-position | left / right | left | Icon position |
| image | String | | Trigger image URL (supports placeholders) |
| image-shape | square / circle / rounded | square | Image shape |
| visible | boolean / String | true | Visibility condition |
| enabled | boolean / String | true | Availability condition |
| datasource | Reference | | Datasource ID for placeholder resolution |
| model | resolve/edit/filter/multi/datasource | | Model for placeholder resolution |
| class | String | | CSS class |
| style | String | | CSS style |

Body: `<menu-item>`, `<button>`, `<divider>`, `<link>`, `<group>`, `<dropdown-menu>` (nested)

```xml
<dropdown-menu name="More" icon="fa fa-ellipsis-v">
    <menu-item name="Reports"><open-page page-id="reports"/></menu-item>
    <divider/>
    <menu-item name="Settings"><open-page page-id="settings"/></menu-item>
</dropdown-menu>
```

---

## Full Example

`mainMenu.menu.xml`:
```xml
<?xml version='1.0' encoding='UTF-8'?>
<nav xmlns="http://n2oapp.net/framework/config/schema/menu-3.0">
    <menu-item name="Home" icon="fa fa-home">
        <open-page page-id="index"/>
    </menu-item>
    <menu-item name="Employees" icon="fa fa-users">
        <open-page page-id="employees"/>
    </menu-item>
    <group label="References" collapsible="true" default-state="collapsed" icon="fa fa-book">
        <menu-item name="Departments"><open-page page-id="departments"/></menu-item>
        <menu-item name="Positions"><open-page page-id="positions"/></menu-item>
    </group>
    <dropdown-menu name="Reports" icon="fa fa-chart-bar">
        <menu-item name="Monthly"><open-page page-id="reportMonthly"/></menu-item>
        <menu-item name="Annual"><open-page page-id="reportAnnual"/></menu-item>
    </dropdown-menu>
</nav>
```

`userMenu.menu.xml`:
```xml
<?xml version='1.0' encoding='UTF-8'?>
<extra-menu xmlns="http://n2oapp.net/framework/config/schema/menu-3.0">
    <menu-item name="{username}" icon="fa fa-user" datasource="currentUser" model="resolve">
        <open-page page-id="profile"/>
    </menu-item>
    <divider/>
    <link label="Documentation" icon="fa fa-book" target="newWindow">
        <a href="https://n2o.i-novus.ru/docs/"/>
    </link>
    <button label="Logout" icon="fa fa-sign-out">
        <invoke operation-id="logout"/>
    </button>
</extra-menu>
```

`application.xml`:
```xml
<header title="My App">
    <nav ref-id="mainMenu"/>
    <extra-menu ref-id="userMenu"/>
</header>
```

## See Also
- `application.md` — `<header>`, `<sidebars>` that reference menu files
- `action.md` — action elements used inside menu items (`<open-page>`, `<invoke>`, `<a>`, etc.)
- `button.md` — regular toolbar buttons (different context, but similar action syntax)
