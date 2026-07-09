# Migration Guide

## page-3.0 → page-4.0
| Change | Before (3.0) | After (4.0) |
|---|---|---|
| Datasource extraction | Inline in widget | In `<datasources>` at page level |
| Widget references data | `query-id`, `object-id` on widget | `datasource="dsId"` on widget |
| Widget filter syntax | `<pre-filters>` in widget | `<filters>` in `<datasource>` |
| Submit | On widget | In `<datasource>` via `<submit>` |
| Actions | In widget `<toolbar>` | Can be in page-level `<actions>` + `action-id` |
| Events | Not available | `<events>` with `<on-change>` |
| Controls: filter syntax | `<pre-filters>` inside control | `<filters>` inside control (control-3.0) |
| Dependency: `<fetch-value>` | `<pre-filters>` inside dependency | `<filters>` inside dependency (control-3.0) |

> **`<pre-filters>` → `<filters>` applies everywhere. The internal XSD type is still named preFiltersDefinition, but the XML element is <filters> in control-3.0.

**Complete list of controls and dependencies with <filters> (formerly <pre-filters>):**

| XML Element | Type | Note |
|---|---|---|
| `<select>` | control | `query-id` + `<filters>` |
| `<input-select>` | control | `query-id` + `<filters>` |
| `<input-select-tree>` | control | `query-id` + `<filters>` |
| `<radio-group>` | control | `query-id` + `<filters>` |
| `<checkbox-group>` | control | `query-id` + `<filters>` |
| `<slider>` | control | `query-id` + `<filters>` |
| `<auto-complete>` | control | `query-id` + `<filters>` |
| `<fetch-value>` | dependency | `query-id` (обязателен) + `<filters>` |

### Example Migration
**Before (page-3.0):**
```xml
<simple-page xmlns="http://n2oapp.net/framework/config/schema/page-3.0">
    <table query-id="employees" object-id="employee" size="10">
        <pre-filters>
            <eq field-id="org.id" value="{orgId}"/>
        </pre-filters>
        <columns>...</columns>
    </table>
</simple-page>
```

**After (page-4.0):**
```xml
<page xmlns="http://n2oapp.net/framework/config/schema/page-4.0">
    <datasources>
        <datasource id="ds1" query-id="employees" object-id="employee" size="10">
            <filters><eq field-id="org.id" value="{orgId}"/></filters>
        </datasource>
    </datasources>
    <regions><table datasource="ds1"><columns>...</columns></table></regions>
</page>
```

**Control filters before (page-3.0 / control-2.x):**
```xml
<input-select id="diagnosis" query-id="diagnosis">
    <pre-filters>
        <eq field-id="disease" value="{disease.id}" required="true"/>
    </pre-filters>
    <dependencies>
        <fetch-value query-id="diagnosis" on="disease" value-field-id="oneElement">
            <pre-filters>
                <eq field-id="onlyOne" value="true" required="true"/>
            </pre-filters>
        </fetch-value>
    </dependencies>
</input-select>
```

**Control filters after (page-4.0 / control-3.0):**
```xml
<input-select id="diagnosis" query-id="diagnosis">
    <filters>
        <eq field-id="disease" value="{disease.id}" required="true"/>
    </filters>
    <dependencies>
        <fetch-value query-id="diagnosis" on="disease" value-field-id="oneElement">
            <filters>
                <eq field-id="onlyOne" value="true" required="true"/>
            </filters>
        </fetch-value>
    </dependencies>
</input-select>
```

---

## object-3.0 → object-4.0
| Change | Before (3.0) | After (4.0) |
|---|---|---|
| Field mapping | `<field id="name" mapping="body.name"/>` | `<field id="name" mapping="['body.name']"/>` |
| Nested fields | Flat with complex mapping | `<reference id="addr" object-id="address">` |
| List fields | Not supported | `<list id="contacts" object-id="contact">` |
| Validation groups | `<validations activate="whiteList">` | `<validations white-list="v1,v2"/>` |

---

## query-4.0 → query-5.0
| Change | Before (4.0) | After (5.0) |
|---|---|---|
| Field structure | `<field><select/><sorting/><filters/></field>` | Flat attributes on `<field>` |
| Select mapping | `<select mapping="[0]" default-value="..."/>` | `expression="..."` on field |
| Sort mapping | `<sorting mapping="[0]">e.name</sorting>` | `sorting-expression="e.name"` on field |
| Filter body | `<filters><filter type="eq"><body>e.id = :id</body></filter></filters>` | `search-expression="e.id"` + separate `<filter>` in `<filters>` |

### Example Migration
**Before (query-4.0):**
```xml
<field id="name">
    <select mapping="[0]">e.name</select>
    <sorting mapping="[0]">e.name</sorting>
    <filters>
        <filter type="like"><body>e.name like '%' || :name || '%'</body></filter>
    </filters>
</field>
```

**After (query-5.0):**
```xml
<field id="name" domain="string" expression="e.name" sorting-expression="e.name" search-expression="e.name"/>
        <!-- Plus in <filters> section: -->
<filter field-id="name" type="like" filter-id="nameLike"/>
```

---

## application-2.0 → application-3.0
| Change | Before (2.0) | After (3.0) |
|---|---|---|
| Sidebar | Single `<sidebar>` | `<sidebars>` with multiple `<sidebar path="...">` |
| Sidebar toggling | `toggled-state`, `default-state` limited | Full: none/micro/mini/maxi + `toggle-on-hover` |
| App datasources | Not available | `<datasources>` in `<application>` |

---

## `target` → `new-window` (link open behavior. Since 7.30.x version!)
The `target` attribute is unified across all components. The `newWindow` value is **deprecated** and no longer supported — opening a link in a new browser tab is now a separate boolean attribute `new-window`. The `self` value is now emitted as `self` (previously `_self`), and `application` remains the default.

| Component | Attribute change | New default |
|---|---|---|
| `<open-page>` action | `target` **removed**, `new-window` added | `new-window="false"` |
| `<search>` (header) action | `target` **removed**, `new-window` added | `new-window="false"` |
| `<a>` action | `target` default `self` → `application`; `new-window` added; `newWindow` unsupported | `target="application"` |
| `<output-list>` field | `target` default `newWindow` → `application`; `new-window` added | `target="application"` |
| `<link>` cell / `<list>` cell link | `new-window` added; `newWindow` for `target` unsupported | `target="application"` |
| `<link>` menu item (`<nav>`) | `target` default `newWindow` → `application`; `new-window` added | `target="application"` |

**How to migrate any `target="newWindow"`:**

| Before | After |
|---|---|
| `target="newWindow"` (on `<open-page>` / `<search>`) | `new-window="true"` |
| `target="newWindow"` (on `<a>` / `<output-list>` / cell `<link>` / menu `<link>`) | `new-window="true" target="self"` |
| `target="application"` (was explicit default) | *(remove — it is the default)* |

> On components where `application` was **not** the previous default (`<a>`, `<output-list>`, menu `<link>`), add `target="self"` alongside `new-window="true"` to preserve the old open-in-same-tab behavior.

### 1. `<open-page>` — `target` removed, `new-window` added

| Before | After |
|---|---|
| `target="newWindow"` | `new-window="true"` |
| `target="application"` *(default)* | *(default — remove)* |

```xml
<!-- Before -->                         
<open-page page-id="report" target="newWindow"/>
<!-- After -->
<open-page page-id="report" new-window="true"/>
```

### 2. `<a>` — default `self` → `application`; `newWindow` unsupported

| Before | After |
|---|---|
| `target="newWindow"` | `new-window="true" target="self"` |
| `target="self"` *(default)* | `target="self"` |
| `target="application"` | *(default — remove)* |

```xml
<!-- Before -->
<a href="/api/document/{id}" target="newWindow"/>
<!-- After -->
<a href="/api/document/{id}" new-window="true" target="self"/>
```

### 3. `<output-list>` (field) — default `newWindow` → `application`; `newWindow` unsupported

| Before | After |
|---|---|
| `target="newWindow"` *(default)* | `new-window="true" target="self"` |
| `target="self"` | `target="self"` |
| `target="application"` | *(default — remove)* |

```xml
<!-- Before -->
<output-list id="links" target="newWindow"/>
<!-- After -->
<output-list id="links" new-window="true" target="self"/>
```

### 4. `<search>` (header search panel) — `target` removed, `new-window` added

| Before | After |
|---|---|
| `target="newWindow"` | `new-window="true"` |
| `target="application"` *(default)* | *(must be removed)* |

```xml
<!-- Before -->
<search query-id="global" target="newWindow"/>
<!-- After -->
<search query-id="global" new-window="true"/>
```

### 5. `<link>` cell / `<link>` inside a `<list>` cell — `new-window` added

| Before | After |
|---|---|
| `target="newWindow"` | `new-window="true" target="self"` |
| `target="self"` | `target="self"` |
| `target="application"` *(default)* | *(default — remove)* |

```xml
<!-- Before -->
<link url="/doc/{id}" target="newWindow"/>
<!-- After -->
<link url="/doc/{id}" new-window="true" target="self"/>
```

### 6. `<link>` menu item in a `<nav>` region — default `newWindow` → `application`

| Before | After |
|---|---|
| `target="newWindow"` *(default)* | `new-window="true" target="self"` |
| `target="self"` | `target="self"` |
| `target="application"` | *(default — remove)* |

```xml
<!-- Before -->
<link label="Docs" target="newWindow">
    <a href="https://n2o.i-novus.ru/docs/"/>
</link>
<!-- After -->
<link label="Docs" new-window="true" target="self">
    <a href="https://n2o.i-novus.ru/docs/"/>
</link>
```

Default properties: `n2o.api.action.link.new_window`, `n2o.api.action.open_page.new_window`, `n2o.api.cell.link.new_window`, `n2o.api.menu.link.new_window`, `n2o.api.control.output_list.new_window` (all `false`); `*.target` defaults are `application`.

---

## Relative URL resolution (routing mode. Since 7.30.x version!)
The way relative `url`/`href` values are resolved changed for elements carrying a `url`/`href` (e.g. `<a>`, `<link>`, `<alert>`, `<print>`, `<image>`, `<html>`). Relative paths now follow standard web semantics instead of always being appended to the current page path.

For a page with `url="/page1"` (directory `/page1/`… note: no trailing slash means the last segment is dropped):

| Before | After |
|---|---|
| `href="./open"` on `url="/page1"` → `/page1/open` | → `/open` |
| To keep `/page1/open` | page must declare `url="/page1/"` (trailing slash) |

Resolution examples for `baseUrl=/page1/page2/page3` (no slash, directory `/page1/page2/`):

| href/url | Result |
|---|---|
| `page4` | `/page1/page2/page4` |
| `/page4` | `/page4` |
| `./page4` | `/page1/page2/page4` |
| `../page4` | `/page1/page4` |

**Compatibility:** set `n2o.config.routing_mode=old` to keep the previous behavior (will be removed in 7.31 — migrate before then).

**Access schema impact:** `<url-access>` patterns now need the exact path. Routes for `open-page`/`show-modal`/`open-drawer` still always end with a slash.

| Before | After |
|---|---|
| `<url-access pattern="/path/create*"/>` | `<url-access pattern="/path/create/*"/>` |

---

## Key Namespace Changes
Always update `xmlns` when migrating:
```
page-3.0 → page-4.0:       .../page-3.0 → .../page-4.0
query-4.0 → query-5.0:     .../query-4.0 → .../query-5.0
object-3.0 → object-4.0:   .../object-3.0 → .../object-4.0
application-2.0 → application-3.0: .../application-2.0 → .../application-3.0
```

## See Also
- `page.md` — current page-4.0 structure
- `query.md` — current query-5.0 structure
- `object.md` — current object-4.0 structure
- `application.md` — current application-3.0 structure
