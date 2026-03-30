# Common Settings (application.properties)

All settings use `n2o.` prefix in `application.properties`.

## Core Settings
```properties
# Config source directories
n2o.config.path=classpath*:META-INF/conf/              # XML config root
n2o.config.monitoring.enabled=true                       # Hot-reload on change

# Homepage
n2o.homepage.id=index                                    # Welcome page id

# Access
n2o.access.schema-id=                                   # Access schema file id
n2o.access.deny-objects=false
n2o.access.deny-pages=false
n2o.access.deny-urls=false

# Localization
n2o.i18n.default-locale=ru
```

## API Defaults — Widgets
```properties
n2o.api.widget.auto_focus=false
n2o.api.widget.table.size=10
n2o.api.widget.table.text_wrap=true
n2o.api.widget.table.selection=active
n2o.api.widget.table.auto_select=true
n2o.api.widget.table.checkboxes=false
n2o.api.widget.table.check_on_select=false
n2o.api.widget.table.sticky_header=false
n2o.api.widget.table.sticky_footer=false
n2o.api.widget.table.scrollbar_position=bottom
n2o.api.widget.table.children.toggle=collapse
n2o.api.widget.column.alignment=left
n2o.api.widget.table.column.resizable=false
n2o.api.widget.form.unsaved_data_prompt=false
n2o.api.widget.form.size=1
n2o.api.widget.list.size=5
n2o.api.widget.cards.size=10
n2o.api.widget.cards.vertical_align=top
n2o.api.widget.tiles.colsLg=4
n2o.api.widget.tiles.size=10
```

## API Defaults — Regions
```properties
n2o.api.region.line.collapsible=true
n2o.api.region.line.has_separator=true
n2o.api.region.line.expand=true
n2o.api.region.panel.collapsible=true
n2o.api.region.panel.open=true
n2o.api.region.panel.routable=true
n2o.api.region.tabs.always_refresh=false
n2o.api.region.tabs.lazy=true
n2o.api.region.tabs.routable=true
n2o.api.region.tabs.scrollbar=false
```

## API Defaults — Pages
```properties
n2o.api.page.show_title=true
```

## API Defaults — Actions
```properties
n2o.api.action.close.with_prompt=true
n2o.api.action.open_page.target=application
n2o.api.action.show_modal.modal_size=lg
n2o.api.action.show_modal.has_header=true
n2o.api.action.show_modal.scrollbar=false
n2o.api.action.show_modal.overlay=true
n2o.api.action.open_drawer.width=
n2o.api.action.open_drawer.placement=right
n2o.api.action.open_drawer.closable=true
```

## API Defaults — Fields
```properties
n2o.api.control.input_text.length=
n2o.api.control.input_text.autocomplete=off
n2o.api.control.text_area.min_rows=3
n2o.api.control.text_area.max_rows=10
n2o.api.control.date_time.date_format=DD.MM.YYYY
n2o.api.control.date_time.time_format=
n2o.api.control.date_time.utc=false
n2o.api.control.checkbox.unchecked=null
n2o.api.control.file_upload.multi=false
n2o.api.control.file_upload.ajax=true
n2o.api.control.file_upload.upload_url=/n2o/data/files
n2o.api.control.file_upload.delete_url=/n2o/data/files/delete
```

## Data Provider Settings
```properties
# REST
n2o.api.data.rest.base-url=http://localhost:8080
n2o.api.data.rest.date-format=yyyy-MM-dd'T'HH:mm:ss

# SQL datasource (Spring JDBC)
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=user
spring.datasource.password=pass
spring.datasource.driver-class-name=org.postgresql.Driver

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/mydb

# GraphQL
n2o.api.data.graphql.endpoint=/graphql
```

## UI / Frontend Settings
```properties
n2o.ui.message.position=topRight
n2o.ui.message.placement=top
n2o.ui.header.fixed=false
n2o.ui.sidebar.fixed=false
n2o.ui.breadcrumbs.max-count=3
```

## See Also
- Each ref file lists relevant settings inline
- `dataprovider.md` — connection settings
- `access.md` — access control settings
