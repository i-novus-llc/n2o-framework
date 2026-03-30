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
| `<authenticated>` | Access for any authenticated user |
| `<permit-all>` | Public access (no auth required) |

## Access Types

### `<page-access>` — Page Visibility
```xml
<page-access page-id="adminPanel"/>
<page-access page-id="*"/><!-- All pages -->
```

### `<object-access>` — Operation Permissions
```xml
<object-access object-id="employee" operations="save,delete"/>
<object-access object-id="*" operations="*"/><!-- All operations on all objects -->
```

### `<url-access>` — URL Pattern Access
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
In page XML, use `visible` with security expressions:
```xml
<button label="Delete" visible="#{hasRole('admin')}">...</button>
<input-text id="secret" visible="#{hasPermission('secret.view')}"/>
```

## See Also
- `application.md` — application structure
- `settings.md` — access-related settings
- `page.md` — pages controlled by access rules
- `object.md` — operations controlled by access rules
