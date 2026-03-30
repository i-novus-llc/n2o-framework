# Snippet Templates

Quick-start XML skeletons for common UI patterns. Copy and adapt.

## See Also
- `field.md` — field attributes and dependencies
- `widget.md` — widget configuration details
- `action.md` — action types
- `object.md` — operations and validations
- `query.md` — query field and filter definitions
- `dataprovider.md` — provider placeholder syntax
- `example.md` — full 4-file CRUD example

---

## 1. Filterable Table with CRUD

A standard list page with search filters, sortable columns, create/edit modals, and delete.

```xml
<?xml version='1.0' encoding='UTF-8'?>
<page xmlns="http://n2oapp.net/framework/config/schema/page-4.0"
      name="__ENTITY_PLURAL__">
    <datasources>
        <datasource id="ds" query-id="__queryId__" object-id="__objectId__" size="10"/>
    </datasources>
    <regions>
        <table datasource="ds">
            <filters>
                <input-text id="name" label="Name" filter-id="nameLike"/>
                <!-- Add more filters as needed -->
                <search-buttons/>
            </filters>
            <columns>
                <column text-field-id="name" label="Name" sorting-direction="asc"/>
                <!-- Add more columns -->
            </columns>
            <rows>
                <click>
                    <show-modal page-id="__cardPageId__" modal-size="lg">
                        <params><path-param name="id" value="{id}"/></params>
                    </show-modal>
                </click>
            </rows>
            <toolbar place="topLeft">
                <button label="Create" color="primary" icon="fa fa-plus">
                    <show-modal page-id="__cardPageId__" modal-size="lg"/>
                </button>
                <button label="Delete" color="danger" icon="fa fa-trash"
                        disable-on-empty-model="true">
                    <invoke operation-id="delete" confirm="true"
                            confirm-text="Delete {name}?" refresh-on-success="true"/>
                </button>
            </toolbar>
            <pagination show-count="always"/>
        </table>
    </regions>
</page>
```

---

## 2. Edit/Create Card (Modal Form)

Standard form for creating/editing a single entity.

```xml
<?xml version='1.0' encoding='UTF-8'?>
<simple-page xmlns="http://n2oapp.net/framework/config/schema/page-4.0"
             name="__ENTITY__ Card">
    <form query-id="__queryId__" object-id="__objectId__">
        <datasource>
            <filters><eq field-id="id" param="id"/></filters>
        </datasource>
        <fields>
            <row>
                <col size="6"><input-text id="__field1__" label="__Label1__" required="true"/></col>
                <col size="6"><input-text id="__field2__" label="__Label2__"/></col>
            </row>
            <!-- Add more rows/fields -->
        </fields>
        <toolbar place="bottomRight">
            <button label="Save" color="primary" validate="true">
                <invoke operation-id="save" close-on-success="true"/>
            </button>
            <button label="Cancel"><close/></button>
        </toolbar>
    </form>
</simple-page>
```

---

## 3. Master-Detail (Table + Form on Same Page)

Left table selects a record, right form shows details.

```xml
<?xml version='1.0' encoding='UTF-8'?>
<page xmlns="http://n2oapp.net/framework/config/schema/page-4.0"
      name="Master-Detail">
    <datasources>
        <datasource id="masterDs" query-id="__masterQuery__" size="10"/>
        <datasource id="detailDs" query-id="__detailQuery__" object-id="__detailObject__">
            <filters><eq field-id="id" value="{id}" ref-datasource="masterDs"/></filters>
            <dependencies><fetch on="masterDs" model="resolve"/></dependencies>
        </datasource>
    </datasources>
    <regions>
        <row>
            <col size="5">
                <table datasource="masterDs">
                    <columns>
                        <column text-field-id="name" label="Name"/>
                    </columns>
                </table>
            </col>
            <col size="7">
                <form datasource="detailDs">
                    <fields>
                        <input-text id="name" label="Name"/>
                        <!-- Detail fields -->
                    </fields>
                    <toolbar place="bottomRight">
                        <button label="Save" color="primary" validate="true">
                            <invoke operation-id="save" refresh-datasources="masterDs"/>
                        </button>
                    </toolbar>
                </form>
            </col>
        </row>
    </regions>
</page>
```

---

## 4. Master-Detail with Sub-Table (Parent + Children)

Table of orders, click to see order items below.

```xml
<?xml version='1.0' encoding='UTF-8'?>
<page xmlns="http://n2oapp.net/framework/config/schema/page-4.0"
      name="Orders">
    <datasources>
        <datasource id="ordersDs" query-id="orders" object-id="order" size="10"/>
        <datasource id="itemsDs" query-id="orderItems" object-id="orderItem" size="20">
            <filters><eq field-id="orderId" value="{id}" ref-datasource="ordersDs"/></filters>
            <dependencies><fetch on="ordersDs" model="resolve"/></dependencies>
        </datasource>
    </datasources>
    <regions>
        <table datasource="ordersDs" name="Orders">
            <columns>
                <column text-field-id="number" label="Order #"/>
                <column text-field-id="date" label="Date"><text format="date DD.MM.YYYY"/></column>
                <column text-field-id="total" label="Total"><text format="number 0,0.00"/></column>
            </columns>
        </table>
        <line label="Order Items" collapsible="true">
            <table datasource="itemsDs" name="Items">
                <columns>
                    <column text-field-id="product.name" label="Product"/>
                    <column text-field-id="quantity" label="Qty"/>
                    <column text-field-id="price" label="Price"><text format="number 0,0.00"/></column>
                </columns>
                <toolbar place="topLeft">
                    <button label="Add Item" color="primary">
                        <show-modal page-id="orderItemCard">
                            <params><path-param name="orderId" value="{id}"/></params>
                        </show-modal>
                    </button>
                </toolbar>
            </table>
        </line>
    </regions>
</page>
```

---

## 5. Tree + Form (Tree Selection → Detail Form)

```xml
<?xml version='1.0' encoding='UTF-8'?>
<page xmlns="http://n2oapp.net/framework/config/schema/page-4.0"
      name="Categories">
    <datasources>
        <datasource id="treeDs" query-id="categories" size="100"/>
        <datasource id="formDs" query-id="categories" object-id="category">
            <filters><eq field-id="id" value="{id}" ref-datasource="treeDs"/></filters>
            <dependencies><fetch on="treeDs" model="resolve"/></dependencies>
        </datasource>
    </datasources>
    <regions>
        <row>
            <col size="4">
                <tree datasource="treeDs" parent-field-id="parentId"
                      label-field-id="name" value-field-id="id"
                      icon-field-id="icon" multi-select="false">
                    <toolbar place="topLeft">
                        <button label="Add" color="primary" icon="fa fa-plus">
                            <show-modal page-id="categoryCard"/>
                        </button>
                    </toolbar>
                </tree>
            </col>
            <col size="8">
                <form datasource="formDs" name="Category Details">
                    <fields>
                        <input-text id="name" label="Name" required="true"/>
                        <input-select id="parent" label="Parent"
                                      query-id="categories" label-field-id="name"/>
                        <text-area id="description" label="Description"/>
                    </fields>
                    <toolbar place="bottomRight">
                        <button label="Save" color="primary" validate="true">
                            <invoke operation-id="save" refresh-datasources="treeDs"/>
                        </button>
                    </toolbar>
                </form>
            </col>
        </row>
    </regions>
</page>
```

---

## 6. Tabs with Multiple Widgets

Entity card with tabbed sections.

```xml
<?xml version='1.0' encoding='UTF-8'?>
<page xmlns="http://n2oapp.net/framework/config/schema/page-4.0"
      name="Employee Profile">
    <datasources>
        <datasource id="mainDs" query-id="employees" object-id="employee">
            <filters><eq field-id="id" param="id"/></filters>
        </datasource>
        <datasource id="contactsDs" query-id="contacts" object-id="contact" size="20">
            <filters><eq field-id="employeeId" value="{id}" ref-datasource="mainDs"/></filters>
        </datasource>
        <datasource id="docsDs" query-id="documents" size="20">
            <filters><eq field-id="employeeId" value="{id}" ref-datasource="mainDs"/></filters>
        </datasource>
    </datasources>
    <regions>
        <form datasource="mainDs">
            <fields>
                <row>
                    <col size="6"><input-text id="name" label="Name" required="true"/></col>
                    <col size="6"><date-time id="birthday" label="Birthday"/></col>
                </row>
            </fields>
        </form>
        <tabs>
            <tab name="Contacts" id="contacts">
                <table datasource="contactsDs">
                    <columns>
                        <column text-field-id="type" label="Type"/>
                        <column text-field-id="value" label="Contact"/>
                    </columns>
                    <toolbar place="topLeft">
                        <button label="Add" color="primary">
                            <show-modal page-id="contactCard">
                                <params><path-param name="employeeId" value="{id}"/></params>
                            </show-modal>
                        </button>
                    </toolbar>
                </table>
            </tab>
            <tab name="Documents" id="docs">
                <table datasource="docsDs">
                    <columns>
                        <column text-field-id="title" label="Title"/>
                        <column text-field-id="date" label="Date"><text format="date DD.MM.YYYY"/></column>
                    </columns>
                </table>
            </tab>
        </tabs>
        <toolbar place="bottomRight">
            <button label="Save" color="primary" validate="true">
                <invoke operation-id="save" close-on-success="true" datasource="mainDs"/>
            </button>
            <button label="Cancel"><close/></button>
        </toolbar>
    </regions>
</page>
```

---

## 7. Dependent Selects (Cascade)

Region → City → Street cascade.

```xml
<fields>
    <input-select id="region" label="Region" query-id="regions" label-field-id="name"/>
    <input-select id="city" label="City" query-id="cities" label-field-id="name">
        <filters><eq field-id="regionId" value="{region.id}"/></filters>
        <dependencies><reset on="region"/></dependencies>
    </input-select>
    <input-select id="street" label="Street" query-id="streets" label-field-id="name">
        <filters><eq field-id="cityId" value="{city.id}"/></filters>
        <dependencies><reset on="city"/></dependencies>
    </input-select>
</fields>
```

---

## 8. Computed Fields

Auto-calculated total, conditional visibility.

```xml
<fields>
    <input-text id="price" label="Price" domain="numeric"/>
    <input-text id="quantity" label="Quantity" domain="integer"/>
    <input-text id="total" label="Total" domain="numeric" enabled="false">
        <dependencies>
            <set-value on="price,quantity">
                (parseFloat(price) || 0) * (parseInt(quantity) || 0)
            </set-value>
        </dependencies>
    </input-text>
    <input-text id="discount" label="Discount %" domain="numeric">
        <dependencies>
            <visibility on="total">total > 10000</visibility>
        </dependencies>
    </input-text>
</fields>
```

---

## 9. Multi-Set (Inline Editable List)

Editing a list of contacts inside a form.

```xml
<fields>
    <input-text id="name" label="Name" required="true"/>
    <multi-set id="contacts" label="Contacts" children-label="Contact #{index}"
               add-label="+ Add Contact" can-remove-first="false">
        <row>
            <col size="4">
                <select id="type" label="Type">
                    <options>
                        <option id="phone" name="Phone"/>
                        <option id="email" name="Email"/>
                        <option id="telegram" name="Telegram"/>
                    </options>
                </select>
            </col>
            <col size="8">
                <input-text id="value" label="Value" required="true"/>
            </col>
        </row>
    </multi-set>
</fields>
```

---

## 10. Status Column with Color Badges

```xml
<columns>
    <column text-field-id="name" label="Name"/>
    <column text-field-id="status.name" label="Status">
        <badge color-field-id="status.color" shape="rounded" position="left"/>
    </column>
    <column text-field-id="priority" label="Priority">
        <text>
            <switch value-field-id="priority">
                <case value="high" color="danger"/>
                <case value="medium" color="warning"/>
                <case value="low" color="success"/>
                <default color="secondary"/>
            </switch>
        </text>
    </column>
</columns>
```

---

## 11. REST Provider CRUD (Object + Query Skeletons)

### Object
```xml
<?xml version='1.0' encoding='UTF-8'?>
<object xmlns="http://n2oapp.net/framework/config/schema/object-4.0" name="__Entity__">
    <operations>
        <operation id="save">
            <invocation><rest method="POST">/api/__entities__</rest></invocation>
            <in>
                <field id="id" domain="integer" mapping="['body.id']"/>
                <field id="name" domain="string" mapping="['body.name']"/>
            </in>
        </operation>
        <operation id="update">
            <invocation><rest method="PUT">/api/__entities__/{id}</rest></invocation>
            <in>
                <field id="id" domain="integer"/>
                <field id="name" domain="string" mapping="['body.name']"/>
            </in>
        </operation>
        <operation id="delete">
            <invocation><rest method="DELETE">/api/__entities__/{id}</rest></invocation>
            <in><field id="id" domain="integer"/></in>
        </operation>
    </operations>
</object>
```

### Query
```xml
<?xml version='1.0' encoding='UTF-8'?>
<query xmlns="http://n2oapp.net/framework/config/schema/query-5.0" name="__Entities__">
    <list><rest method="GET" query="/api/__entities__?limit={limit}&amp;offset={offset}&amp;name={nameLike}"/></list>
    <count><rest method="GET" query="/api/__entities__/count?name={nameLike}"/></count>
    <unique><rest method="GET" query="/api/__entities__/{id}"/></unique>
    <fields>
        <field id="id" domain="integer"/>
        <field id="name" domain="string"/>
    </fields>
    <filters>
        <filter field-id="id" type="eq" domain="integer"/>
        <filter field-id="name" type="like" filter-id="nameLike"/>
    </filters>
</query>
```
