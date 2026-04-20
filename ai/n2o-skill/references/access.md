# Access Control (access-2.0)

Schema: `http://n2oapp.net/framework/config/schema/access-2.0`
Files: `[id].access.xml`

## `<access>` Root Structure
```xml
<?xml version='1.0' encoding='UTF-8'?>
<access xmlns="http://n2oapp.net/framework/config/schema/access-2.0">
    <permission id="users.read">
        <page-access>...</page-access>
    </permission>
    <role id="admin">
        <page-access>...</page-access>
        <object-access>...</object-access>
        <url-access>...</url-access>
    </role>
    <authenticated>
        <page-access>...</page-access>
    </authenticated>
    <permit-all>
        <page-access>...</page-access>
    </permit-all>
</access>
```

## Security Contexts
| Element | Description |
|---|---|
| `<role id="...">` | Access for users with specific role |
| `<permission id="...">` | Access for users with specific permission |
| `<user username="...">` | Access for a specific named user |
| `<authenticated>` | Access for any authenticated user |
| `<anonymous>` | Access for anonymous (unauthenticated) users |
| `<permit-all>` | Public access (no auth required) |

### `<role>` / `<permission>` Attributes
| Attribute | Type | Description |
|---|---|---|
| id | String | Identifier of the role or permission (required) |
| name | String | Display name |

### `<user>` Attributes
| Attribute | Type | Description |
|---|---|---|
| username | String | Username to grant access to (required) |

## Access Types

### `<page-access>` — Page Visibility
| Attribute | Type | Description |
|---|---|---|
| page-id | String | Identifier of the page to grant access to (required). Use `*` for all pages. |

```xml
<page-access page-id="adminPanel"/>
<page-access page-id="*"/><!-- All pages -->
```

### `<object-access>` — Operation Permissions
| Attribute | Type | Description |
|---|---|---|
| object-id | String | Identifier of the object to grant access to (required). Use `*` for all objects. |
| operations | String | Comma-separated operation identifiers. Use `*` for all. If omitted, read-only access. |
| remove-filters | String | Comma-separated list of filters to cancel (granting broader data access) |

```xml
<object-access object-id="employee" operations="save,delete"/>
<object-access object-id="*" operations="*"/><!-- All operations on all objects -->
```

### `<url-access>` — URL Pattern Access
| Attribute | Type | Description |
|---|---|---|
| pattern | String | URL pattern to grant access to. Use `*` to match any single path segment, `**` for multi-segment. |

```xml
<url-access pattern="/admin/**"/>
<url-access pattern="/api/users/*"/>
```

## Full Example
```xml
<?xml version='1.0' encoding='UTF-8'?>
<access xmlns="http://n2oapp.net/framework/config/schema/access-2.0">
    <permission id="employee.delete">
        <object-access object-id="employee" operations="delete"/>
    </permission>
    <role id="admin">
        <page-access page-id="*"/>
        <object-access object-id="*" operations="*"/>
    </role>
    <role id="user">
        <page-access page-id="employees"/>
        <page-access page-id="employeeCard"/>
        <object-access object-id="employee" operations="save"/>
    </role>
    <authenticated>
        <page-access page-id="profile"/>
        <page-access page-id="dashboard"/>
    </authenticated>
    <permit-all>
        <page-access page-id="login"/>
        <page-access page-id="index"/>
    </permit-all>
</access>
```

## Settings
```properties
n2o.access.schema-id=myAccess        # access file id (without .access.xml)
n2o.access.deny-objects=false         # deny unspecified object operations
n2o.access.deny-pages=false           # deny unspecified pages
n2o.access.deny-urls=false            # deny unspecified URLs
```

## Widget/Field Level Access

Use security XML attributes on buttons, fields, and other elements (from `security-1.0` schema):
| Attribute | Type | Description |
|---|---|---|
| roles | String | Comma-separated roles required |
| permissions | String | Comma-separated permissions required |
| usernames | String | Comma-separated usernames allowed |
| authenticated | boolean | Visible/enabled for authenticated users only |
| anonymous | boolean | Visible/enabled for anonymous users only |
| permit-all | boolean | Accessible to all users |
| denied | boolean | Access denied for everyone |

Or use `visible` / `enabled` with Spring Security expressions:
```xml
<button label="Delete" visible="#{hasRole('admin')}">...</button>
<input-text id="secret" visible="#{hasPermission('secret.view')}"/>
```

## See Also
- `application.md` — application structure
- `settings.md` — access-related settings
- `page.md` — pages controlled by access rules
- `object.md` — operations controlled by access rules
