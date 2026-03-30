# Complete CRUD Example

A full working example with 4 files: list page, card page, object, and query.

## 1. employees.page.xml — List Page
```xml
<?xml version='1.0' encoding='UTF-8'?>
<page xmlns="http://n2oapp.net/framework/config/schema/page-4.0"
      name="Employees">
    <datasources>
        <datasource id="ds1" query-id="employees" object-id="employee" size="10"/>
    </datasources>
    <regions>
        <table datasource="ds1">
            <filters>
                <input-text id="name" label="Name" filter-id="nameLike"/>
                <select id="department" label="Department" query-id="departments"
                        label-field-id="name" filter-id="deptId" cleanable="true"/>
                <search-buttons/>
            </filters>
            <columns>
                <column text-field-id="name" label="Name" sorting-direction="asc"/>
                <column text-field-id="department.name" label="Department"/>
                <column text-field-id="birthday" label="Birthday">
                    <text format="date DD.MM.YYYY"/>
                </column>
                <column text-field-id="salary" label="Salary">
                    <text format="number 0,0.00"/>
                </column>
            </columns>
            <rows>
                <click>
                    <show-modal page-id="employeeCard" modal-size="lg">
                        <params><path-param name="id" value="{id}"/></params>
                    </show-modal>
                </click>
            </rows>
            <toolbar place="topLeft">
                <button label="Create" color="primary" icon="fa fa-plus">
                    <show-modal page-id="employeeCard" modal-size="lg"/>
                </button>
                <button label="Delete" color="danger" icon="fa fa-trash"
                        disable-on-empty-model="true">
                    <invoke operation-id="delete" close-on-success="false"
                            confirm="true" confirm-text="Delete employee {name}?"
                            refresh-on-success="true"/>
                </button>
            </toolbar>
            <pagination show-count="always"/>
        </table>
    </regions>
</page>
```

## 2. employeeCard.page.xml — Edit/Create Card
```xml
<?xml version='1.0' encoding='UTF-8'?>
<simple-page xmlns="http://n2oapp.net/framework/config/schema/page-4.0"
             name="Employee Card">
    <form query-id="employees" object-id="employee">
        <datasource>
            <filters>
                <eq field-id="id" param="id"/>
            </filters>
        </datasource>
        <fields>
            <row>
                <col size="6">
                    <input-text id="name" label="Full Name" required="true" length="200"/>
                </col>
                <col size="6">
                    <date-time id="birthday" label="Birthday" date-format="DD.MM.YYYY"/>
                </col>
            </row>
            <row>
                <col size="6">
                    <input-select id="department" label="Department"
                                  query-id="departments" label-field-id="name"
                                  type="single"/>
                </col>
                <col size="6">
                    <input-money id="salary" label="Salary" suffix=" ₽"
                                 thousands-separator=" " integer-limit="10"/>
                </col>
            </row>
            <text-area id="notes" label="Notes" min-rows="3" max-rows="6"/>
        </fields>
        <toolbar place="bottomRight">
            <button label="Save" color="primary" icon="fa fa-save" validate="true">
                <invoke operation-id="save" close-on-success="true"/>
            </button>
            <button label="Cancel">
                <close/>
            </button>
        </toolbar>
    </form>
</simple-page>
```

## 3. employee.object.xml — Object (Operations + Validations)
```xml
<?xml version='1.0' encoding='UTF-8'?>
<object xmlns="http://n2oapp.net/framework/config/schema/object-4.0"
        name="Employee" table-name="employees">
    <operations>
        <operation id="save" name="Save Employee"
                   success-text="Employee «{name}» saved">
            <invocation>
                <sql>
                    INSERT INTO employees (id, name, birthday, dept_id, salary, notes)
                    VALUES (:id, :name, :birthday, :deptId, :salary, :notes)
                    ON CONFLICT (id) DO UPDATE
                    SET name = :name, birthday = :birthday, dept_id = :deptId,
                        salary = :salary, notes = :notes
                </sql>
            </invocation>
            <in>
                <field id="id" domain="integer" default-value="nextval('employees_id_seq')"/>
                <field id="name" domain="string" required="true"/>
                <field id="birthday" domain="localdate"/>
                <field id="department.id" domain="integer" mapping="['deptId']"/>
                <field id="salary" domain="numeric"/>
                <field id="notes" domain="string"/>
            </in>
            <validations>
                <validation ref-id="nameRequired"/>
            </validations>
        </operation>

        <operation id="delete" name="Delete Employee" confirm="true"
                   confirm-text="Delete employee?" success-text="Deleted">
            <invocation>
                <sql>DELETE FROM employees WHERE id = :id</sql>
            </invocation>
            <in>
                <field id="id" domain="integer" required="true"/>
            </in>
        </operation>
    </operations>

    <validations>
        <mandatory id="nameRequired" field-id="name" message="Name is required"/>
        <condition id="salaryPositive" message="Salary must be positive" on="salary"
                   severity="warning">
            salary == null || salary > 0
        </condition>
    </validations>
</object>
```

## 4. employees.query.xml — Query
```xml
<?xml version='1.0' encoding='UTF-8'?>
<query xmlns="http://n2oapp.net/framework/config/schema/query-5.0"
       name="Employees" object-id="employee">
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
        <field id="id" domain="integer" expression="e.id"/>
        <field id="name" domain="string" expression="e.name"
               sorting-expression="e.name" search-expression="e.name"/>
        <field id="birthday" domain="localdate" expression="e.birthday"
               sorting-expression="e.birthday"/>
        <field id="department.id" domain="integer" expression="e.dept_id"/>
        <field id="department.name" domain="string" expression="d.name"
               sorting-expression="d.name" search-expression="d.name"/>
        <field id="salary" domain="numeric" expression="e.salary"
               sorting-expression="e.salary"/>
        <field id="notes" domain="string" expression="e.notes"/>
    </fields>

    <filters>
        <filter field-id="id" type="eq" domain="integer"/>
        <filter field-id="name" type="like" filter-id="nameLike"/>
        <filter field-id="department.id" type="eq" filter-id="deptId" domain="integer"/>
        <filter field-id="birthday" type="more" filter-id="birthdayFrom" domain="localdate"/>
        <filter field-id="birthday" type="less" filter-id="birthdayTo" domain="localdate"/>
    </filters>
</query>
```

## SQL DDL (for reference)
```sql
CREATE TABLE departments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL
);

CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    birthday DATE,
    dept_id INTEGER REFERENCES departments(id),
    salary NUMERIC(15,2),
    notes TEXT
);
```

## See Also
- `snippets.md` — more patterns (master-detail, tree+form, REST, etc.)
- `page.md`, `widget.md`, `field.md` — attribute details
- `query.md`, `object.md` — data layer details
- `dataprovider.md` — provider alternatives (REST, GraphQL)
