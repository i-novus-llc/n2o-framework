# N2O Framework Skill

## Description
Skill for working with N2O Framework — a Java+ReactJS library for building web applications via declarative XML DSL. 
Full XML API reference is split into focused section files in the `references/` directory. 
Use this skill for: creating pages, objects, queries, and other N2O XML configuration files; 
updating and modifying existing configurations; finding and fixing errors; migrating between framework versions; 
and answering questions about the framework. 
Triggers include: any mention of "n2o", ".page.xml", ".object.xml", ".query.xml", "n2o framework", "n2oapp", or requests to create UI with XML in Java projects.

**Version**: 7.29+ | **Current schemas**: page-4.0, query-5.0, object-4.0, application-3.0, widget-5.0, region-3.0, fieldset-5.0, control-3.0, action-2.0
**Documentation**: https://n2o.i-novus.ru/docs/

## Trigger Conditions
Activate when user mentions: N2O, n2o, N2O Framework, net.n2oapp, *.page.xml, *.object.xml, *.query.xml, *.menu.xml, *.access.xml, application.xml, XML-based UI framework, n2oapp.net, declarative XML UI configuration, `<datasource>`, `<invoke>`, `<simple-page>`, `<show-modal>`.

## Interaction Rules
1. ALWAYS ask clarifying questions before generating XML: artifact type, data provider, entity fields, operations needed, UI layout
2. Read ONLY the reference sections relevant to the current task (see routing table below)
3. For error diagnosis, read `references/errors.md`
4. For migration, read `references/migration.md`
5. Always use LATEST schema versions unless user specifies otherwise

## Reference Routing Table

Read the relevant `references/*.md` file(s) based on what the user needs:

| User Task | Read These References |
|---|---|
| Create/edit application.xml, header, sidebar, footer | `references/application.md` |
| Create/edit *.page.xml, page layout, datasources on page | `references/page.md` |
| Layout with tabs, panels, collapsible sections, grids | `references/region.md` |
| Tables, forms, lists, cards, trees, calendars, charts | `references/widget.md` |
| Input fields, selects, date pickers, checkboxes, file upload | `references/field.md` |
| Buttons, toolbars, confirmation dialogs | `references/button.md` |
| Actions: invoke, open-page, show-modal, close, set-value, if/else | `references/action.md` |
| Table cell rendering (badges, icons, images, links) | `references/cell.md` |
| Create/edit *.object.xml, operations, validations | `references/object.md` |
| Create/edit *.query.xml, filters, sorting, pagination | `references/query.md` |
| SQL, REST, GraphQL, test providers, placeholder syntax | `references/dataprovider.md` |
| Datasource types (inherited, app, browser-storage, stomp) | `references/datasource.md` |
| Fieldsets: set, line, multi-set, row/col grid layout | `references/fieldset.md` |
| Access control, roles, permissions | `references/access.md` |
| application.properties, default settings | `references/settings.md` |
| Schema migration (page-3→4, object-3→4, query-4→5) | `references/migration.md` |
| Debugging, error diagnosis checklist | `references/errors.md` |
| Full working CRUD example (4 files) | `references/example.md` |
| Quick-start skeletons (master-detail, tree+form, REST CRUD, etc.) | `references/snippets.md` |

**For a typical CRUD task**, read: `references/page.md` + `references/widget.md` + `references/field.md` + `references/action.md` + `references/object.md` + `references/query.md` + `references/dataprovider.md`. 
Or just read `references/snippets.md` for a matching template and adapt it.

## XML Validator
After generating XML, ALWAYS run the validator before presenting to user:
```bash
# From skill directory:
python validate_n2o.py <file.xml> [file2.xml ...]
python validate_n2o.py --dir <directory>

# Full path (Windows):
python "%USERPROFILE%\.claude\skills\n2o-skill\validate_n2o.py" <file.xml>

# Full path (Linux/Mac):
python3 ~/.claude/skills/n2o-skill/validate_n2o.py <file.xml>
```
Fix any ERRORs. WARNs are informational — review but may be intentional.

## Quick Facts (no ref file needed)

### File Naming
| File Pattern | Type | Schema |
|---|---|---|
| `[id].page.xml` | Page | `page-4.0` |
| `[id].query.xml` | Query | `query-5.0` |
| `[id].object.xml` | Object | `object-4.0` |
| `application.xml` | App structure | `application-3.0` |
| `[id].menu.xml` | Menu | `menu-3.0` |
| `[id].access.xml` | Access | `access-2.0` |

### Schema Namespaces (xmlns)
```
http://n2oapp.net/framework/config/schema/page-4.0
http://n2oapp.net/framework/config/schema/query-5.0
http://n2oapp.net/framework/config/schema/object-4.0
http://n2oapp.net/framework/config/schema/application-3.0
http://n2oapp.net/framework/config/schema/widget-5.0
http://n2oapp.net/framework/config/schema/region-3.0
http://n2oapp.net/framework/config/schema/fieldset-5.0
http://n2oapp.net/framework/config/schema/control-3.0
http://n2oapp.net/framework/config/schema/action-2.0
```

### Placeholder Syntax by Provider
| Provider | Syntax | Example |
|---|---|---|
| SQL | `:name` | `WHERE id = :id` |
| REST | `{name}` | `/api/users/{id}` |
| GraphQL | `$$name` | `query { user(id: $$id) }` |

### Domain Types
`string`, `integer`, `long`, `short`, `byte`, `numeric` (BigDecimal), `boolean`, `date`, `localdate`, `localdatetime`, `object`. Add `[]` for arrays: `integer[]`, `string[]`.

### XML Escaping Reminder
`&&` → `&amp;&amp;`, `<` → `&lt;`, `>` → `&gt;`, `&` → `&amp;` in attribute values and conditions.
