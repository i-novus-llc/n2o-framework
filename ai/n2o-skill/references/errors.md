# Error Diagnosis Checklist

When N2O XML configuration fails, check these items systematically:

## 1. Schema Version Mismatch
**Symptom**: Unrecognized element/attribute errors
**Fix**: Verify `xmlns` matches expected schema version. Use latest:
```
page-4.0, query-5.0, object-4.0, application-3.0, widget-5.0, region-3.0, fieldset-5.0, control-3.0, action-2.0
```

## 2. Missing `id` Field in Query
**Symptom**: Table shows no data, empty response, `id` not found errors
**Fix**: EVERY query MUST have `<field id="id">`:
```xml
<fields><field id="id" domain="integer" expression="e.id"/>...</fields>
```

## 3. Wrong Placeholder Syntax
**Symptom**: Parameters not substituted, SQL errors, empty values
**Fix**: Match syntax to provider:
- SQL: `:paramName`
- REST: `{paramName}`
- GraphQL: `$$paramName`

## 4. XML Escaping
**Symptom**: Parse errors, `&` or `<` breaks XML
**Fix**: Always escape in attribute values and element text:
- `&&` → `&amp;&amp;`
- `<` → `&lt;`
- `>` → `&gt;`
- `&` → `&amp;`

## 5. Field ID / Mapping Mismatch
**Symptom**: Data not displayed, null values, wrong columns
**Fix**: Check that:
- Page field `id` matches query field `id`
- Object `<in>` field ids match form field ids
- SQL mapping uses `['providerName']` syntax when ids differ
- Nested fields use dot notation: `department.id`, `department.name`

## 6. Datasource Not Connected
**Symptom**: Widget shows empty, no data loaded
**Fix**: Verify:
- Widget has `datasource="dsId"` pointing to existing datasource
- Datasource has `query-id` for data-displaying widgets
- Datasource has `object-id` for operation-executing widgets
- For `<simple-page>`: datasource is inline in widget, not in `<datasources>`

## 7. Filter Field Reference
**Symptom**: Filters have no effect, data not filtered
**Fix**: Check:
- `filter-id` in `<filters>` matches `field-id` in `<filter>` of query
- Filter `value="{fieldRef}"` references existing field in source datasource
- `search-expression` is defined on the query field

## 8. Operation Not Found
**Symptom**: "Operation not found" on save/delete
**Fix**:
- `operation-id` in `<invoke>` matches `<operation id="...">` in object
- Widget/page datasource has `object-id` pointing to correct object file
- Object file is named `[objectId].object.xml`

## 9. Route / URL Issues
**Symptom**: Page not found, wrong page opens, params not passed
**Fix**:
- `<path-param>` names match route placeholders
- Sidebar `path` uses correct wildcard pattern (`*` vs `**`)
- Page `route` doesn't conflict with other routes

## 10. Dependencies Not Triggering
**Symptom**: set-value/visibility/enabling doesn't react
**Fix**:
- `on="fieldId"` lists the triggering field(s) (comma-separated)
- JS expression uses field ids directly (not `model.fieldName`)
- For cross-datasource: specify `datasource` attribute

## 11. Modal/Drawer Doesn't Show Data
**Symptom**: Modal opens but form is empty
**Fix**:
- `<path-param name="id" value="{id}"/>` passes the selected row's id
- Target page datasource has `<filters><eq field-id="id" param="id"/></filters>`
- Target query has `<unique>` defined
- Target query has filter `<filter field-id="id" type="eq"/>`

## 12. Validation Not Working
**Symptom**: Can submit invalid data
**Fix**:
- Validation `id` is referenced in operation's `<validations>`
- `on="field"` matches the field to validate
- JS expression returns `true` for VALID state (not for invalid)
- For `<constraint>`: out field `domain="boolean"` returns true when INVALID

## 13. Table Toolbar Generate Not Working
**Symptom**: `generate="crud"` shows no buttons
**Fix**: Ensure datasource has `object-id` set. CRUD generation needs an object.

## 14. Date Formatting
**Symptom**: Date shows as raw timestamp, wrong format
**Fix**:
- Field: `date-format="DD.MM.YYYY"` (moment.js format, case-sensitive)
- Cell: `<text format="date DD.MM.YYYY"/>`
- Domain: use `localdate` for date-only, `localdatetime` for date+time

## 15. File Naming
**Symptom**: "Configuration not found"
**Fix**: File names must match references:
- `query-id="employees"` → `employees.query.xml`
- `object-id="employee"` → `employee.object.xml`
- `page-id="employeeCard"` → `employeeCard.page.xml`
- Files must be on config classpath (default: `META-INF/conf/`)

## See Also
- `query.md` — field and filter definitions
- `object.md` — operation structure
- `dataprovider.md` — placeholder syntax
- `migration.md` — schema version changes
