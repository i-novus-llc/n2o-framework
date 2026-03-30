# Objects (object-4.0)

Schema: `http://n2oapp.net/framework/config/schema/object-4.0`
Files: `[id].object.xml`

Objects define server-side operations and validations on data entities.

## `<object>` Root Element
| Attribute | Type | Description |
|---|---|---|
| name | String | Object display name |
| table-name | String | DB table name (for SQL provider shorthand) |
| entity-class | String | Entity class associated with object |
| service-class | String | Service class performing actions on object |
| service-name | String | Service bean name |
| app-name | String | Application name containing object |
| module-name | String | Module name containing object |

Body: `<fields>`, `<operations>`, `<validations>`

## `<fields>` — Object Fields Definition
Contains: `<field>`, `<reference>`, `<list>`, `<set>`

### `<field>` in Fields
| Attribute | Type | Description |
|---|---|---|
| id | String | Field identifier (required) |
| domain | String | Data type |
| mapping | String | Provider mapping |
| default-value | String | Default value |
| normalize | String | SpEL expression applied before mapping |
| required | boolean | Required field (default: false) |
| enabled | String | Condition for mapping execution |

### `<reference>`, `<list>`, `<set>` — Composite Fields
| Attribute | Type | Description |
|---|---|---|
| id | String | Reference identifier (required) |
| object-id | String | Referenced object ID |
| entity-class | String | Entity class of referenced object |
| mapping | String | Provider mapping for composite field |
| required | boolean | Required field (default: false) |
| enabled | String | Condition for mapping execution |
| normalize | String | SpEL expression applied before mapping |

Body: `<field>`, `<reference>`, `<list>`, `<set>` (nested)

```xml
<fields>
    <field id="id" domain="integer"/>
    <field id="name" domain="string"/>
    <reference id="department" object-id="dept">
        <field id="id" domain="integer"/>
        <field id="name" domain="string"/>
    </reference>
    <list id="contacts" object-id="contact">
        <field id="phone" domain="string"/>
    </list>
    <set id="tags" object-id="tag">
        <field id="name" domain="string"/>
    </set>
</fields>
```

## `<operation>` — Server-Side Operation
| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Operation identifier (required) | |
| name | String | Display name | |
| description | String | Operation description | |
| success-text | String | Success message template `{field}` | "Данные сохранены" |
| success-title | String | Success message title | |
| fail-text | String | Failure message | "Не удалось выполнить действие" |
| fail-title | String | Failure message title | |

Body: `<invocation>` (required), `<in>`, `<out>`, `<fail-out>`, `<validations>`

### `<invocation>` — Provider Call
| Attribute | Type | Description |
|---|---|---|
| result-mapping | String | Mapping of operation result |
| result-normalize | String | SpEL normalization of result |

Contains exactly one data provider element: `<sql>`, `<rest>`, `<java>`, `<test>`, `<mongodb>`, `<camunda>`, `<graphql>`. See `dataprovider.md` for provider types.
```xml
<invocation>
    <sql>INSERT INTO employees (name, age) VALUES (:name, :age)</sql>
</invocation>
```

### `<in>` — Input Parameters
Contains: `<field>`, `<reference>`, `<list>`, `<set>`

#### `<field>` in Operations (Input)
| Attribute | Type | Description |
|---|---|---|
| id | String | Field identifier (required) |
| domain | String | Data type |
| mapping | String | Provider mapping (e.g. `['orgId']` for SQL, `['body.org_id']` for REST) |
| default-value | String | Default value |
| normalize | String | SpEL expression |
| required | boolean | Required field |
| enabled | String | Condition for mapping execution |
| param | String | URL parameter name |
| validation-fail-key | String | Server validation error code |

Body: `<switch>` (optional)

```xml
<in>
    <field id="id" domain="integer" required="true"/>
    <field id="name" domain="string" default-value="N/A"/>
    <field id="org.id" domain="integer" mapping="['orgId']"/>
    <reference id="address" object-id="address">
        <field id="city" domain="string"/>
        <field id="street" domain="string"/>
    </reference>
    <list id="contacts" object-id="contact">
        <field id="phone" domain="string"/>
    </list>
</in>
```

### `<out>` — Output Parameters
Contains: `<field>`, `<reference>`, `<list>`, `<switch>`

#### `<field>` in Operations (Output)
| Attribute | Type | Description |
|---|---|---|
| id | String | Field identifier (required) |
| domain | String | Data type |
| mapping | String | Provider mapping |
| default-value | String | Default value |
| normalize | String | SpEL expression applied after mapping |

```xml
<out><field id="id" domain="integer"/></out>
```

### `<fail-out>` — Error Output Parameters
| Attribute | Type | Description |
|---|---|---|
| id | String | Parameter identifier (required) |
| mapping | String | Provider mapping |
| normalize | String | SpEL expression applied after mapping |

```xml
<fail-out>
    <field id="errorCode" mapping="['code']"/>
    <field id="errorMessage" mapping="['message']"/>
</fail-out>
```

### `<switch>` — Conditional Value
Used inside `<field>` for conditional default values.
```xml
<field id="status" domain="string">
    <switch>
        <case value="active">Active Status</case>
        <case value="inactive">Inactive Status</case>
        <default>Unknown</default>
    </switch>
</field>
```

### Operation Example
```xml
<operations>
    <operation id="save" name="Save Employee" success-text="Employee {name} saved">
        <invocation>
            <sql>INSERT INTO employees (name, birthday, dept_id)
                 VALUES (:name, :birthday, :deptId)
                 ON CONFLICT (id) DO UPDATE
                 SET name = :name, birthday = :birthday, dept_id = :deptId</sql>
        </invocation>
        <in>
            <field id="id" domain="integer"/>
            <field id="name" domain="string" required="true"/>
            <field id="birthday" domain="localdate"/>
            <field id="department.id" domain="integer" mapping="['deptId']"/>
        </in>
        <validations white-list="ageCheck, uniqueName"/>
    </operation>

    <operation id="delete" name="Delete">
        <invocation>
            <sql>DELETE FROM employees WHERE id = :id</sql>
        </invocation>
        <in><field id="id" domain="integer" required="true"/></in>
    </operation>
</operations>
```

## `<validations>` — Server-Side Validations
Types: `<condition>`, `<constraint>`, `<mandatory>`

### Common Validation Attributes
| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Validation id (required) | |
| severity | danger/warning/info/success | Error level | danger |
| message | String | Error message (supports `{field}`) | |
| title | String | Error message title | |
| field-id | String | Field to display message under | |
| enabled | String | Enable/disable validation | true |
| side | client/server/client,server | Execution side | client,server |
| server-moment | String | Server execution moment | |

Server moments: `before-operation`, `before-query`, `after-success-query`, `after-fail-query`, `after-fail-operation`, `after-success-operation`

### `<condition>` — Expression Validation
| Attribute | Type | Description |
|---|---|---|
| on | String (comma-sep) | Fields that trigger validation |
| src | String | Path to JS file with validation condition |

Body: JS or SpEL expression (must return true for valid)
```xml
<condition id="ageCheck" message="Age must be 18+" on="age" severity="danger">
    age >= 18
</condition>
```

### `<constraint>` — DB Constraint Check
| Attribute | Type | Description |
|---|---|---|
| result | String | Validation result expression |

Body: `<invocation>` (provider), `<in>`, `<out>`
```xml
<constraint id="uniqueName" message="Name already exists" severity="danger">
    <invocation><sql>SELECT count(*) > 0 FROM employees WHERE name = :name AND id != :id</sql></invocation>
    <in>
        <field id="name" domain="string"/>
        <field id="id" domain="integer"/>
    </in>
    <out><field id="result" domain="boolean"/></out>
</constraint>
```

### `<mandatory>` — Required Field Validation
| Attribute | Type | Description |
|---|---|---|
| field-id | String | Field that must be filled (required) |

```xml
<mandatory id="nameRequired" field-id="name" message="Name is required"/>
```

### Referencing Validations in Operations
Use `white-list` or `black-list` attribute on `<validations>` element inside operation:
```xml
<operation id="save">
    <invocation>...</invocation>
    <in>...</in>
    <validations white-list="ageCheck, uniqueName"/>
</operation>

<operation id="quickSave">
    <invocation>...</invocation>
    <in>...</in>
    <validations black-list="heavyValidation"/>
</operation>
```

## Full Object Example
```xml
<?xml version='1.0' encoding='UTF-8'?>
<object xmlns="http://n2oapp.net/framework/config/schema/object-4.0"
        name="Employee" table-name="employees">
    <operations>
        <operation id="save" success-text="Saved: {name}">
            <invocation><sql>INSERT INTO employees (name, age) VALUES (:name, :age)
                ON CONFLICT (id) DO UPDATE SET name = :name, age = :age</sql></invocation>
            <in>
                <field id="id" domain="integer"/>
                <field id="name" domain="string" required="true"/>
                <field id="age" domain="integer"/>
            </in>
            <validations white-list="ageCheck"/>
        </operation>
        <operation id="delete">
            <invocation><sql>DELETE FROM employees WHERE id = :id</sql></invocation>
            <in><field id="id" domain="integer" required="true"/></in>
        </operation>
    </operations>
    <validations>
        <condition id="ageCheck" message="Age must be >= 18" on="age" severity="danger">age >= 18</condition>
    </validations>
</object>
```

## See Also
- `dataprovider.md` — provider types for invocations
- `query.md` — queries associated with objects
- `action.md` — invoke action that calls operations
- `snippets.md` — REST provider CRUD skeleton
