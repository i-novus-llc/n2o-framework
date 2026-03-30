# Data Providers

Data providers define how N2O communicates with backend services. Used inside `<invocation>` (objects) and `<list>`/`<count>`/`<unique>` (queries).

## Provider Types Summary
| Provider | Placeholder | Use Case |
|---|---|---|
| `<sql>` | `:name` | Direct SQL queries |
| `<rest>` | `{name}` | REST API calls |
| `<graphql>` | `$$name` | GraphQL API |
| `<test>` | — | Mock/test JSON data |
| `<java>` | — | Spring Bean method calls |
| `<mongodb>` | `#name` | MongoDB operations |

---

## `<sql>` — SQL Provider
Placeholder syntax: `:paramName`
Special placeholders (query only): `:select`, `:filters`, `:sorting`, `:limit`, `:offset`

**Mapping**: Use `mapping="['providerParamName']"` when field id differs from SQL parameter name.

### Query Example
```xml
<list>
    <sql>SELECT :select FROM employees e
         LEFT JOIN departments d ON d.id = e.dept_id
         :filters ORDER BY :sorting LIMIT :limit OFFSET :offset</sql>
</list>
```

### Operation Examples
```xml
<!-- Insert -->
<invocation>
    <sql>INSERT INTO employees (name, age, dept_id) VALUES (:name, :age, :deptId)</sql>
</invocation>
<in>
    <field id="name" domain="string"/>
    <field id="age" domain="integer"/>
    <field id="department.id" domain="integer" mapping="['deptId']"/>
</in>

<!-- Upsert -->
<invocation>
    <sql>INSERT INTO employees (id, name) VALUES (:id, :name)
         ON CONFLICT (id) DO UPDATE SET name = :name</sql>
</invocation>

<!-- Delete -->
<invocation>
    <sql>DELETE FROM employees WHERE id = :id</sql>
</invocation>

<!-- Stored procedure -->
<invocation>
    <sql>SELECT my_procedure(:id, :name)</sql>
</invocation>
```

### SQL Escaping Reminder
Use `&amp;&amp;` for `&&`, `&lt;` for `<`, `&gt;` for `>` in XML.

---

## `<rest>` — REST Provider
| Attribute | Type | Description |
|---|---|---|
| method | GET/POST/PUT/DELETE/PATCH/HEAD | HTTP method |
| query | String | URL path (for queries) |
| mapping | String | Result field mapping |
| content-type | String | Request content type |
| proxy-host / proxy-port | String | Proxy settings |

Placeholder syntax: `{paramName}`
Special placeholders (query only): `{limit}`, `{offset}`, `{page}`, `{filters}`, `{sorting}`

### Query Example
```xml
<list>
    <rest method="GET" query="/api/employees?limit={limit}&amp;offset={offset}&amp;name={nameLike}"/>
</list>
<unique>
    <rest method="GET" query="/api/employees/{id}"/>
</unique>
```

### Operation Example
```xml
<invocation>
    <rest method="POST">/api/employees</rest>
</invocation>
<in>
    <field id="name" domain="string" mapping="['body.name']"/>
    <field id="age" domain="integer" mapping="['body.age']"/>
</in>

<invocation>
    <rest method="PUT">/api/employees/{id}</rest>
</invocation>

<invocation>
    <rest method="DELETE">/api/employees/{id}</rest>
</invocation>
```

### REST Response Mapping
```xml
<rest method="GET" query="/api/employees" mapping="data.items"/>
<!-- Extracts list from { "data": { "items": [...] } } -->
```

Settings:
```
n2o.api.data.rest.base-url=http://localhost:8080
n2o.api.data.rest.date-format=yyyy-MM-dd'T'HH:mm:ss
```

---

## `<graphql>` — GraphQL Provider
| Attribute | Type | Description |
|---|---|---|
| endpoint | String | GraphQL endpoint URL |
| operation | query / mutation | GraphQL operation type |

Placeholder syntax: `$$paramName`
Special placeholders (query only): `$$select`, `$$size`, `$$offset`, `$$page`

### Query Example
```xml
<list>
    <graphql operation="query" endpoint="/graphql">
        query { employees(size: $$size, offset: $$offset) { $$select } }
    </graphql>
</list>
<unique>
    <graphql operation="query" endpoint="/graphql">
        query { employee(id: $$id) { $$select } }
    </graphql>
</unique>
```

### Operation Example
```xml
<invocation>
    <graphql operation="mutation" endpoint="/graphql">
        mutation { saveEmployee(name: $$name, age: $$age) { id } }
    </graphql>
</invocation>
<in>
    <field id="name" domain="string"/>
    <field id="age" domain="integer"/>
</in>
```

---

## `<test>` — Test Data Provider
Reads mock data from JSON files. Great for prototyping.

| Attribute | Type | Description |
|---|---|---|
| file | String | Path to JSON file |
| operation | create / update / delete / echo | Test operation |

```xml
<list><test file="data/employees.json"/></list>
<unique><test file="data/employees.json"/></unique>
```
JSON file format: `[ {"id": 1, "name": "John"}, {"id": 2, "name": "Jane"} ]`

### Test Operation
```xml
<invocation><test file="data/employees.json" operation="create"/></invocation>
<invocation><test file="data/employees.json" operation="echo"/></invocation><!-- returns input -->
```

---

## `<java>` — Spring Bean Provider
| Attribute | Type | Description |
|---|---|---|
| class | String | Full class name |
| method | String | Method name |

```xml
<invocation>
    <java class="com.example.EmployeeService" method="save">
        <arguments>
            <argument type="entity" class="com.example.Employee"/>
        </arguments>
    </java>
</invocation>
```

### Spring Data Query
```xml
<list>
    <java class="com.example.EmployeeService" method="findAll">
        <arguments>
            <argument type="criteria" class="com.example.EmployeeCriteria"/>
        </arguments>
    </java>
</list>
```

Argument types: `primitive`, `entity`, `criteria`, `class`

---

## `<mongodb>` — MongoDB Provider
| Attribute | Type | Description |
|---|---|---|
| collection-name | String | MongoDB collection |
| operation | find / insertOne / updateOne / deleteOne / countDocuments | |

```xml
<list>
    <mongodb collection-name="employees" operation="find"/>
</list>
<invocation>
    <mongodb collection-name="employees" operation="insertOne"/>
</invocation>
```

Settings:
```
spring.data.mongodb.uri=mongodb://localhost:27017/mydb
```

## See Also
- `query.md` — queries that use providers for data fetching
- `object.md` — operations that use providers for invocations
- `settings.md` — provider-specific connection settings
- `snippets.md` — REST CRUD skeleton
