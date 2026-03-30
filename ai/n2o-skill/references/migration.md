# Migration Guide

## page-3.0 → page-4.0
| Change | Before (3.0) | After (4.0) |
|---|---|---|
| Datasource extraction | Inline in widget | In `<datasources>` at page level |
| Widget references data | `query-id`, `object-id` on widget | `datasource="dsId"` on widget |
| Filter syntax | `<pre-filters>` in widget | `<filters>` in `<datasource>` |
| Submit | On widget | In `<datasource>` via `<submit>` |
| Actions | In widget `<toolbar>` | Can be in page-level `<actions>` + `action-id` |
| Events | Not available | `<events>` with `<on-change>` |

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
