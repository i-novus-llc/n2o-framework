# Queries (query-5.0)

Schema: `http://n2oapp.net/framework/config/schema/query-5.0`
Files: `[id].query.xml`

Queries define how to fetch data from the server for widgets.

## `<query>` Root Element
| Attribute | Type | Description | Default |
|---|---|---|---|
| object-id | Reference | Associated object | |
| route | String | URL route | query id |

Body: `<list>`, `<count>`, `<unique>`, `<fields>`, `<filters>`

## Data Fetching Elements

Each element contains exactly one provider invocation (`<sql>`, `<rest>`, `<graphql>`, etc.). See `dataprovider.md`.

### `<list>` — Fetch Multiple Records

| Attribute | Type | Description | Default |
|---|---|---|---|
| filters | String | Required filter IDs (comma-separated) | |
| result-mapping | String | SpEL mapping of result list | |
| result-normalize | String | SpEL normalization of result list | |
| count-mapping | String | SpEL mapping of total count | |
| asc-expression | String | ASC sort direction expression | asc |
| desc-expression | String | DESC sort direction expression | desc |
| additional-mapping | String | SpEL mapping of additional info | |

```xml
<list>
    <sql>SELECT :select FROM employees e :filters ORDER BY :sorting LIMIT :limit OFFSET :offset</sql>
</list>
```

### `<count>` — Count Records (for pagination)

| Attribute | Type | Description | Default |
|---|---|---|---|
| filters | String | Required filter IDs (comma-separated) | |
| count-mapping | String | SpEL mapping of count result | |

```xml
<count>
    <sql>SELECT count(*) FROM employees e :filters</sql>
</count>
```

### `<unique>` — Fetch Single Record

| Attribute | Type | Description | Default |
|---|---|---|---|
| filters | String | Required filter IDs (comma-separated) | |
| result-mapping | String | SpEL mapping of result | |
| result-normalize | String | SpEL normalization of result | |

```xml
<unique>
    <sql>SELECT :select FROM employees e WHERE e.id = :id</sql>
</unique>
```

**CRITICAL**: SQL queries use special placeholders that the framework replaces:
| Placeholder | Replaced With |
|---|---|
| `:select` | Comma-separated list of field expressions |
| `:filters` | WHERE clause from filters (prefixed with `WHERE` or `AND`) |
| `:sorting` | ORDER BY clause |
| `:limit` | Page size |
| `:offset` | Records to skip |

## `<fields>` — Query Field Definitions

### `<field>` Attributes
| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Field identifier (required) | |
| name | String | Display label | |
| domain | integer/string/boolean/date/localdate/localdatetime/object/numeric/long/short/byte/integer[]/string[]/boolean[]/date[]/object[] | Data type | |
| default-value | String | Default value if not provided | |
| mapping | String | SpEL mapping from provider | `[id]` |
| select | boolean | Include in SELECT | true |
| select-expression | String | SQL/provider expression for SELECT | |
| normalize | String | SpEL post-processing | |
| sorting | boolean | Field is sortable | false (true if sorting-expression set) |
| sorting-expression | String | Expression for ORDER BY | |
| sorting-mapping | String | Mapping of sort direction | `[id]Direction` |

**CRITICAL**: Every query MUST have `<field id="id">` — the framework requires an `id` field.

```xml
<fields>
    <field id="id" domain="integer" select-expression="e.id"/>
    <field id="name" domain="string" select-expression="e.name" sorting-expression="e.name"/>
    <field id="birthday" domain="localdate" select-expression="e.birthday"/>
    <field id="department.id" domain="integer" select-expression="e.dept_id"/>
    <field id="department.name" domain="string" select-expression="d.name"/>
    <field id="age" domain="integer" select-expression="extract(year from age(e.birthday))" sorting-expression="e.birthday"/>
</fields>
```

### Nested Object Fields
Use dot notation for nested objects:
```xml
<field id="organization.id" domain="integer" select-expression="o.id"/>
<field id="organization.name" domain="string" select-expression="o.name"/>
```
This creates a nested JSON object `{ "organization": { "id": 1, "name": "Acme" } }`.

### Computed Fields
```xml
<field id="fullName" domain="string" select-expression="concat(e.last_name, ' ', e.first_name)"/>
<field id="age" domain="integer" select-expression="extract(year from age(e.birthday))"/>
```

### `<reference>` — Composite Field (nested object)

| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Field identifier (required) | |
| mapping | String | SpEL mapping from provider | `[id]` |
| select | boolean | Include in SELECT | true |
| select-expression | String | Expression for SELECT | |
| select-key | String | Key for select-expression placeholder | |
| normalize | String | SpEL post-processing | |

Body: `<field>`, `<reference>`, `<list>`

```xml
<fields>
    <reference id="organization" mapping="[org]">
        <field id="id" domain="integer" select-expression="o.id"/>
        <field id="name" domain="string" select-expression="o.name"/>
    </reference>
</fields>
```

### `<list>` — List Field (array of objects)

Same attributes as `<reference>`. Used for one-to-many relationships.

```xml
<fields>
    <list id="contacts" mapping="[contactList]">
        <field id="id" domain="integer"/>
        <field id="phone" domain="string"/>
        <field id="type" domain="string"/>
    </list>
</fields>
```

### `<switch>` — Conditional Field Value

Used inside `<field>` to set value based on conditions.

| Element | Description |
|---|---|
| `<case value="...">` | Value to compare against (required attribute), element body is the result |
| `<default>` | Default result if no case matches |

```xml
<field id="statusName" domain="string" select-expression="e.status">
    <switch>
        <case value="1">Active</case>
        <case value="2">Inactive</case>
        <default>Unknown</default>
    </switch>
</field>
```

## `<filters>` — Filter Definitions

### Filter Elements
Filter type is specified by element name: `<eq>`, `<notEq>`, `<like>`, `<likeStart>`, `<in>`, `<notIn>`, `<more>`, `<less>`, `<isNull>`, `<isNotNull>`, `<contains>`, `<overlaps>`, `<eqOrIsNull>`.

| Attribute | Type | Description | Default |
|---|---|---|---|
| field-id | String | Field being filtered (required) | |
| filter-id | String | Filter identifier (for UI) | `{field-id}` |
| default-value | String | Default filter value | |
| domain | integer/string/boolean/date/localdate/localdatetime/object/numeric/long/short/byte/integer[]/string[]/boolean[]/date[]/object[] | Override field domain. Defaults to the `domain` of the referenced `<field>` | |
| mapping | String | SpEL expression for mapping the filter value to the provider field | `['filter-id']` |
| normalize | String | SpEL expression for pre-processing the filter value | |
| required | boolean | Mandatory filter | false |

```xml
<filters>
    <like field-id="name" filter-id="nameLike"/>
    <eq field-id="department.id" filter-id="deptId"/>
    <more field-id="age" filter-id="ageFrom"/>
    <less field-id="age" filter-id="ageTo"/>
    <eq field-id="id" filter-id="id" domain="integer"/>
    <eq field-id="active" default-value="true"/>
    <overlaps field-id="roles" filter-id="roleIds" domain="integer[]"/>
</filters>
```

### How Filters Map to SQL
Each filter generates a part of the `:filters` replacement:
```
<like field-id="name"/>  → e.name LIKE '%' || :nameLike || '%'
<more field-id="age"/>   → extract(year from age(e.birthday)) > :ageFrom
<eq field-id="id"/>      → e.id = :id
```
The `select-expression` from the field definition is used in the WHERE clause.

## Full Query Example
```xml
<?xml version='1.0' encoding='UTF-8'?>
<query xmlns="http://n2oapp.net/framework/config/schema/query-5.0"
       object-id="employee">
    <list>
        <sql>SELECT :select
             FROM employees e
             LEFT JOIN departments d ON d.id = e.dept_id
             :filters
             ORDER BY :sorting
             LIMIT :limit OFFSET :offset</sql>
    </list>
    <count>
        <sql>SELECT count(*)
             FROM employees e
             LEFT JOIN departments d ON d.id = e.dept_id
             :filters</sql>
    </count>
    <unique>
        <sql>SELECT :select
             FROM employees e
             LEFT JOIN departments d ON d.id = e.dept_id
             WHERE e.id = :id</sql>
    </unique>
    <fields>
        <field id="id" domain="integer" select-expression="e.id"/>
        <field id="name" domain="string" select-expression="e.name"
               sorting-expression="e.name"/>
        <field id="birthday" domain="localdate" select-expression="e.birthday"
               sorting-expression="e.birthday"/>
        <field id="department.id" domain="integer" select-expression="e.dept_id"/>
        <field id="department.name" domain="string" select-expression="d.name"
               sorting-expression="d.name"/>
    </fields>
    <filters>
        <eq field-id="id" domain="integer"/>
        <like field-id="name" filter-id="nameLike"/>
        <eq field-id="department.id" filter-id="deptId" domain="integer"/>
        <more field-id="birthday" filter-id="birthdayFrom" domain="localdate"/>
        <less field-id="birthday" filter-id="birthdayTo" domain="localdate"/>
    </filters>
</query>
```

## See Also
- `dataprovider.md` — provider types and placeholder syntax
- `object.md` — associated object for operations
- `field.md` — UI fields that map to query fields
- `datasource.md` — datasources that reference queries
