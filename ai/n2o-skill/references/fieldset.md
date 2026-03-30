# Fieldsets (fieldset-5.0)

Schema: `http://n2oapp.net/framework/config/schema/fieldset-5.0`
Fieldsets organize fields inside forms into visual groups.

## Base Fieldset Attributes
| Attribute | Type | Description | Default |
|---|---|---|---|
| id | String | Fieldset identifier | |
| ref-id | Reference | Parent fieldset file | |
| src | String | React component for fieldset | |
| label | String | Group label (placeholders supported) | |
| description | String | Description text | |
| help | String | Tooltip hint (placeholders supported) | |
| help-trigger | hover/click | Help tooltip trigger | click |
| visible | boolean / String | Visibility (JS) | true |
| enabled | boolean / String | Enabled (JS) | true |
| class / style | String | CSS | |
| depends-on | String (comma-sep) | Dependency fields | |
| field-label-location | left / top | Label position | top |
| field-label-align | left / right | Label alignment | left |
| field-label-width | String (px/em/%) | Label width (for left) | |
| badge | String | Badge text (placeholders supported) | |
| badge-color | String | Badge color | |
| badge-shape | square / circle / rounded | Badge shape | square |
| badge-position | left / right | Badge position | right |
| badge-image | String | Badge image URL | |
| badge-image-position | left / right | Badge image position | left |
| badge-image-shape | square / circle / rounded | Badge image shape | circle |

---

## `<set>` — Simple Field Group
Wraps fields in a bordered/labeled group.
```xml
<set label="Personal Info" description="Basic employee data">
    <input-text id="firstName" label="First Name"/>
    <input-text id="lastName" label="Last Name"/>
    <date-time id="birthday" label="Birthday"/>
</set>
```

## `<line>` — Line Fieldset (Collapsible)
| Attribute | Type | Default |
|---|---|---|
| collapsible | boolean | true |
| has-separator | boolean | true |
| expand | boolean | true |

```xml
<line label="Address" collapsible="true" expand="false">
    <input-text id="city" label="City"/>
    <input-text id="street" label="Street"/>
</line>
```

## `<multi-set>` — Repeating Field Group
For editing arrays/lists of items. Each item is a row of fields. Attribute `id` is required.
| Attribute | Type | Description | Default |
|---|---|---|---|
| children-label | String | Item label template (`{index}`) | |
| first-children-label | String | Label for first item | |
| add-label | String | "Add" button label | |
| can-add | boolean | Show add button | true |
| can-remove | boolean | Show remove buttons | true |
| can-remove-first | boolean | Can remove first item | false |
| can-remove-all | boolean | Show "remove all" button | false |
| remove-all-label | String | "Remove all" button label | |
| can-copy | boolean | Show copy buttons | false |
| primary-key | String | Unique key field | id |
| generate-primary-key | boolean | Auto-generate GUID for key | false |

```xml
<multi-set id="contacts" label="Contacts" children-label="Contact #{index}"
           add-label="Add Contact" can-remove-first="false">
    <input-text id="phone" label="Phone"/>
    <input-text id="email" label="Email"/>
    <select id="type" label="Type"><options>
        <option id="work" name="Work"/><option id="home" name="Home"/>
    </options></select>
</multi-set>
```

## `<row>` + `<col>` — Grid Layout
Arrange fields in columns within a 12-column Bootstrap grid.
```xml
<row>
    <col size="6"><input-text id="firstName" label="First Name"/></col>
    <col size="6"><input-text id="lastName" label="Last Name"/></col>
</row>
<row>
    <col size="4"><date-time id="birthday" label="Birthday"/></col>
    <col size="4"><select id="gender" label="Gender"/></col>
    <col size="4"><input-text id="phone" label="Phone"/></col>
</row>
```

### `<row>` Attributes
| Attribute | Type | Description |
|---|---|---|
| class | String | CSS class |
| style | String | CSS style |

### `<col>` Attributes
| Attribute | Type | Description |
|---|---|---|
| size | Number (1-12) | Column width |
| visible | boolean / String | Visibility |
| class | String | CSS class |
| style | String | CSS style |

## Fieldset Dependencies
```xml
<set label="Extra" depends-on="type">
    <dependencies>
        <visibility>type.id == 'special'</visibility>
    </dependencies>
    <input-text id="extra" label="Extra Info"/>
</set>
```

## Nesting
Fieldsets can be nested:
```xml
<set label="Employee">
    <row>
        <col size="6"><input-text id="name" label="Name"/></col>
        <col size="6"><date-time id="birthday" label="Birthday"/></col>
    </row>
    <line label="Address" collapsible="true">
        <row>
            <col size="8"><input-text id="street" label="Street"/></col>
            <col size="4"><input-text id="zip" label="ZIP"/></col>
        </row>
    </line>
    <multi-set id="contacts" label="Contacts" add-label="+ Contact">
        <row>
            <col size="6"><input-text id="phone" label="Phone"/></col>
            <col size="6"><input-text id="email" label="Email"/></col>
        </row>
    </multi-set>
</set>
```

## See Also
- `field.md` — input controls placed inside fieldsets
- `widget.md` — form widget that contains fieldsets
- `region.md` — row/col layout at region level
- `snippets.md` — multi-set and grid layout examples
