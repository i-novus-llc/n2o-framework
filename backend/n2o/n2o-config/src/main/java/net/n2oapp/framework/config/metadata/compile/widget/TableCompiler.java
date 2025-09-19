package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.FieldsetItem;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.filter_buttons.N2oClearButton;
import net.n2oapp.framework.api.metadata.control.filter_buttons.N2oFilterButtonField;
import net.n2oapp.framework.api.metadata.control.filter_buttons.N2oSearchButtons;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.*;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oAbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oBaseColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oColumnsTableSetting;
import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oResizeTableSetting;
import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oWordWrapTableSetting;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oGroup;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oSubmenu;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.table.*;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.PageIndexScope;
import net.n2oapp.framework.config.metadata.compile.ValidationScope;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.StringUtils.prepareSizeAttribute;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.api.script.ScriptProcessor.buildSwitchExpression;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.initMetaActions;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;
import static org.apache.commons.collections.MapUtils.isNotEmpty;

/**
 * Компиляция таблицы
 */
@Component
public class TableCompiler<D extends Table<?>, S extends N2oTable> extends BaseListWidgetCompiler<D, S> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTable.class;
    }

    @Override
    protected String getPropertyWidgetSrc() {
        return "n2o.api.widget.table.src";
    }

    @Override
    public D compile(S source, CompileContext<?, ?> context, CompileProcessor p) {
        D table = constructTable();

        compileBaseWidget(table, source, context, p);
        N2oAbstractDatasource datasource = getDatasourceById(source.getDatasourceId(), p);
        CompiledQuery query = getQuery(datasource, p);
        CompiledObject object = getObject(source, datasource, p);
        WidgetScope widgetScope = new WidgetScope(
                source.getId(),
                source.getDatasourceId(),
                ReduxModelEnum.FILTER, p
        );
        SubModelsScope subModelsScope = castDefault(p.getScope(SubModelsScope.class), SubModelsScope::new);
        ValidationScope validationScope = castDefault(p.getScope(ValidationScope.class), ValidationScope::new);
        FiltersScope filtersScope = p.getScope(FiltersScope.class);
        TableFiltersScope tableFiltersScope = filtersScope != null
                ? new TableFiltersScope(datasource.getId(), filtersScope)
                : null;

        table.setFilter(initFilter(
                table, source, context, p, widgetScope, query, object,
                new ModelsScope(ReduxModelEnum.FILTER, widgetScope.getClientDatasourceId(), p.getScope(Models.class)),
                subModelsScope,
                new MomentScope(N2oValidation.ServerMomentEnum.BEFORE_QUERY),
                validationScope,
                tableFiltersScope
        ));
        MetaActions widgetActions = initMetaActions(source, p);
        TableWidgetComponent component = table.getComponent();
        compileToolbarAndAction(table, source, context, p, widgetScope, widgetActions, object, null);
        compileColumns(source, context, p, component, query, object, widgetScope, widgetActions, subModelsScope, tableFiltersScope);
        component.setWidth(prepareSizeAttribute(source.getWidth()));
        component.setHeight(prepareSizeAttribute(source.getHeight()));
        component.setTextWrap(castDefault(source.getTextWrap(), p.resolve(property("n2o.api.widget.table.text_wrap"), Boolean.class)));
        component.getBody().setRow(initRows(source, context, p, object, widgetScope, component));
        table.setPaging(compilePaging(source, p.resolve(property("n2o.api.widget.table.size"), Integer.class), p, widgetScope));
        table.setChildren(castDefault(source.getChildren(), () -> p.resolve(property("n2o.api.widget.table.children.toggle"), ChildrenToggleEnum.class)));
        table.setSaveSettings(shouldSaveSettings(source, p));
        component.setAutoSelect(castDefault(source.getAutoSelect(), () -> p.resolve(property("n2o.api.widget.table.auto_select"), Boolean.class)));

        return table;
    }

    private TableWidgetComponent.BodyRow initRows(S source, CompileContext<?, ?> context, CompileProcessor p, CompiledObject object, WidgetScope widgetScope, TableWidgetComponent component) {
        if (source.getRows() == null)
            return component.getBody().getRow();

        TableWidgetComponent.BodyRow row = castDefault(component.getBody().getRow(), TableWidgetComponent.BodyRow::new);
        row.setSrc(source.getRows().getSrc());
        if (source.getRows().getStyle() != null) {
            row.getElementAttributes().put("style", StylesResolver.resolveStyles(p.resolveJS(source.getRows().getStyle())));
        }
        if (source.getRows().getRowClass() != null) {
            row.getElementAttributes().put("className", p.resolveJS(source.getRows().getRowClass()));
        } else if (source.getRows().getColor() != null) {
            Map<Object, String> resolvedCases = new HashMap<>();
            for (String key : source.getRows().getColor().getCases().keySet()) {
                resolvedCases.put(
                        p.resolve(key), source.getRows().getColor().getCases().get(key)
                );
            }
            source.getRows().getColor().setResolvedCases(resolvedCases);
            row.getElementAttributes().put("className", buildSwitchExpression(source.getRows().getColor()));
        }
        row.setClick(compileRowClick(source, context, p, widgetScope, object));
        row.setOverlay(compileRowOverlay(source, context, p, widgetScope, new PageIndexScope(source.getId())));

        return row;
    }

    /**
     * Метод для создания экземпляра клиентской модели таблицы, должен быть переопределен в подклассах
     */
    protected D constructTable() {
        return (D) new Table(new TableWidgetComponent());
    }

    private void compileColumns(N2oTable source, CompileContext<?, ?> context, CompileProcessor p,
                                TableWidgetComponent component, CompiledQuery query, CompiledObject object,
                                Object... scopes) {
        if (source.getColumns() == null)
            return;
        List<AbstractColumn> columns = new ArrayList<>();
        Map<String, String> sortings = new HashMap<>();
        IndexScope columnIndex = new IndexScope();
        CellsScope cellsScope = new CellsScope(new ArrayList<>());

        for (N2oAbstractColumn column : source.getColumns()) {
            columns.add(p.compile(column, context, p, new ComponentScope(column), object, columnIndex, cellsScope, query, scopes));
            if (column instanceof N2oBaseColumn baseColumn && baseColumn.getSortingDirection() != null) {
                String fieldId = castDefault(baseColumn.getSortingFieldId(), baseColumn.getTextFieldId());
                if (fieldId == null)
                    throw new N2oException(String.format("В колонке \"<column>\" c 'id=%s' задан атрибут 'sorting-direction', но не указано поле сортировки. Задайте 'sorting-field-id' или 'text-field-id'",
                            baseColumn.getId()));
                sortings.put(RouteUtil.normalizeParam(fieldId),
                        baseColumn.getSortingDirection().toString().toUpperCase()
                );
            }
        }
        component.getHeader().setCells(columns);
        compileColumnsTableSetting(source.getToolbars(), component.getHeader());
        component.getBody().setCells(cellsScope.getCells());
        if (isNotEmpty(sortings)) {
            passSortingToDatasource(sortings, source, p);
        }
        component.setRowSelection(castDefault(source.getSelection(), () -> p.resolve(property("n2o.api.widget.table.selection"), RowSelectionEnum.class)));
    }

    private void passSortingToDatasource(Map<String, String> sortings, N2oTable source, CompileProcessor p) {
        String sourceDatasourceId = p.getScope(PageScope.class)
                .getWidgetIdSourceDatasourceMap()
                .get(source.getId());

        getDataSourcesScope(p).ifPresent(sc -> sc.get(sourceDatasourceId).setSorting(sortings));
    }

    private AbstractTable.Filter initFilter(Table<?> compiled, N2oTable source, CompileContext<?, ?> context, CompileProcessor p,
                                            WidgetScope widgetScope, CompiledQuery widgetQuery, CompiledObject object,
                                            Object... scopes) {
        List<N2oField> searchButtons = new ArrayList<>();
        if (source.getFilters() != null)
            findSearchButtons(source.getFilters().getItems(), searchButtons);
        if (searchButtons.isEmpty())
            initDefaultSearchButtons(source);
        List<FieldSet> fieldSets = initFieldSets(source.getFilters() == null ? null : source.getFilters().getItems(),
                context, p, widgetScope, widgetQuery, object, scopes);
        if (fieldSets.isEmpty()) {
            return compiled.getFilter();
        }
        AbstractTable.Filter filter = new AbstractTable.Filter();
        filter.setFilterFieldsets(fieldSets);
        filter.setFilterButtonId("filter");
        filter.setFetchOnClear(castDefault(source.getFilters().getFetchOnClear(),
                () -> p.resolve(property("n2o.api.widget.table.fetch_on_clear"), Boolean.class)));
        filter.setBlackResetList(initBlackResetList(searchButtons));
        filter.setFilterPlace(castDefault(source.getFilters().getPlace(), FilterPositionEnum.TOP));
        filter.setFetchOnChange(castDefault(source.getFilters().getFetchOnChange(),
                () -> p.resolve(property("n2o.api.widget.table.fetch_on_change"), Boolean.class)));
        filter.setFetchOnEnter(castDefault(source.getFilters().getFetchOnEnter(),
                () -> p.resolve(property("n2o.api.widget.table.fetch_on_enter"), Boolean.class)));
        initInlineFiltersDatasource(compiled, source, p);

        return filter;
    }

    private void initDefaultSearchButtons(N2oTable source) {
        if (source.getFilters() == null || Boolean.TRUE.equals(source.getFilters().getFetchOnChange()))
            return;
        List<N2oField> searchButtons = new ArrayList<>();
        findSearchButtons(source.getFilters().getItems(), searchButtons);
        if (!searchButtons.isEmpty())
            return;
        SourceComponent[] items = new SourceComponent[source.getFilters().getItems().length + 1];
        System.arraycopy(source.getFilters().getItems(), 0, items, 0, items.length - 1);
        N2oSearchButtons defaultSearchButtons = new N2oSearchButtons();
        defaultSearchButtons.setNoLabelBlock("true");
        items[items.length - 1] = defaultSearchButtons;
        source.getFilters().setItems(items);
    }

    private void findSearchButtons(SourceComponent[] filters, List<N2oField> searchButtons) {
        if (filters == null)
            return;
        for (SourceComponent filter : filters) {
            if (filter instanceof N2oSearchButtons || filter instanceof N2oFilterButtonField) {
                searchButtons.add((N2oField) filter);
            } else if (filter instanceof FieldsetItem fieldsetItem) {
                findSearchButtons(fieldsetItem.getItems(), searchButtons);
            }
        }
    }

    private List<String> initBlackResetList(List<N2oField> searchButtons) {
        List<String> blackResetList = new ArrayList<>();
        searchButtons.forEach(f -> Arrays.stream(getIgnore(f)).collect(Collectors.toCollection(() -> blackResetList)));
        return blackResetList;
    }

    private String[] getIgnore(N2oField field) {
        if (field instanceof N2oSearchButtons searchButtons && searchButtons.getClearIgnore() != null)
            return searchButtons.getClearIgnore();
        if (field instanceof N2oClearButton clearButton && clearButton.getIgnore() != null)
            return clearButton.getIgnore();
        return new String[]{};
    }


    /**
     * Инициализация встроенного источника данных
     */
    protected void initInlineFiltersDatasource(Table<?> compiled, N2oTable source, CompileProcessor p) {
        if (source.getFilters() == null || source.getFilters().getDatasourceId() == null && source.getFilters().getDatasource() == null)
            return;
        String datasourceId = source.getFilters().getDatasourceId();
        if (datasourceId == null) {
            datasourceId = source.getId() + "_filter";
            N2oStandardDatasource datasource = source.getFilters().getDatasource();
            source.getFilters().setDatasource(null);
            datasource.setId(datasourceId);
            source.getFilters().setDatasourceId(datasourceId);
            String finalDatasourceId = datasourceId;
            getDataSourcesScope(p).ifPresent(sc -> sc.put(finalDatasourceId, datasource));
        }
        compiled.setFiltersDatasourceId(getClientDatasourceId(datasourceId, p));
    }

    private Optional<DataSourcesScope> getDataSourcesScope(CompileProcessor p) {
        return Optional.ofNullable(p.getScope(DataSourcesScope.class));
    }

    /**
     * Компиляция поведения при наведении на строку
     */
    protected RowOverlay compileRowOverlay(N2oAbstractListWidget source, CompileContext<?, ?> context,
                                           CompileProcessor p, WidgetScope widgetScope,
                                           PageIndexScope indexScope) {
        if (source.getRows() == null || source.getRows().getRowOverlay() == null)
            return null;

        RowOverlay overlay = new RowOverlay();

        N2oRowOverlay rowOverlay = source.getRows().getRowOverlay();
        overlay.setClassName(rowOverlay.getClassName());
        if (rowOverlay.getToolbar() != null) {
            Toolbar toolbar = p.compile(rowOverlay.getToolbar(), context, widgetScope, indexScope);
            Iterator<List<Group>> iterator = toolbar.values().iterator();
            if (iterator.hasNext())
                overlay.setToolbar(iterator.next());
        }
        return overlay;
    }

    private boolean shouldSaveSettings(S source, CompileProcessor p) {
        if (Boolean.FALSE.equals(p.resolve(property("n2o.api.widget.table.save_settings"), Boolean.class)) ||
            source.getToolbars() == null)
            return false;

        return Arrays.stream(source.getToolbars())
                .flatMap(toolbar -> Arrays.stream(toolbar.getItems()))
                .anyMatch(this::containsSettingToSave);
    }

    private boolean containsSettingToSave(ToolbarItem item) {
        if (item instanceof N2oSubmenu submenu && submenu.getMenuItems() != null)
            return Arrays.stream(submenu.getMenuItems()).anyMatch(this::isSettingToSave);
        if (item instanceof N2oGroup group && group.getItems() != null)
            return Arrays.stream(group.getItems()).anyMatch(this::containsSettingToSave);
        return isSettingToSave(item);
    }

    private boolean isSettingToSave(ToolbarItem item) {
        return item instanceof N2oColumnsTableSetting ||
               item instanceof N2oResizeTableSetting ||
               item instanceof N2oWordWrapTableSetting;
    }

    /**
     * Компилирует настройки колонок таблицы из тулбаров и применяет их к заголовку таблицы.
     *
     * @param toolbars Массив тулбаров, содержащих настройки таблицы
     * @param header   Заголовок таблицы, к которому применяются настройки
     */
    private void compileColumnsTableSetting(N2oToolbar[] toolbars, TableWidgetComponent.TableHeader header) {
        if (toolbars == null) return;
        Arrays.stream(toolbars)
                .filter(toolbar -> toolbar.getItems() != null)
                .flatMap(toolbar -> Arrays.stream(toolbar.getItems()))
                .map(this::getColumnsTableSetting)
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresent(setting -> applyColumnsTableSettings(header, setting));
    }

    public N2oColumnsTableSetting getColumnsTableSetting(ToolbarItem toolbarItem) {
        if (toolbarItem instanceof N2oColumnsTableSetting setting)
            return setting;
        if (toolbarItem instanceof N2oGroup group && group.getItems() != null)
            return Arrays.stream(group.getItems())
                    .map(this::getColumnsTableSetting).filter(Objects::nonNull).findFirst().orElse(null);
        if (toolbarItem instanceof N2oSubmenu i && i.getMenuItems() != null)
            return Arrays.stream(i.getMenuItems())
                    .map(this::getColumnsTableSetting).filter(Objects::nonNull).findFirst().orElse(null);
        return null;
    }

    /**
     * Применяет настройки видимости и блокировки колонок к заголовку таблицы.
     *
     * @param header  Заголовок таблицы для настройки
     * @param setting Исходная модель с настройками колонок
     */
    private static void applyColumnsTableSettings(TableWidgetComponent.TableHeader header, N2oColumnsTableSetting setting) {
        Set<String> locked = setting.getLocked() != null
                ? new HashSet<>(Arrays.asList(setting.getLocked()))
                : new HashSet<>();
        Set<String> defaultVisible = setting.getDefaultValue() != null
                ? new HashSet<>(Arrays.asList(setting.getDefaultValue()))
                : new HashSet<>();

        if (!CollectionUtils.isEmpty(locked) && !CollectionUtils.isEmpty(defaultVisible))
            defaultVisible.addAll(locked);

        processColumnHierarchy(header.getCells(), locked, defaultVisible);
    }

    /**
     * Рекурсивно обрабатывает иерархию колонок, применяя настройки видимости и блокировки.
     *
     * @param columns        Список колонок
     * @param locked         Множество ID заблокированных колонок
     * @param defaultVisible Множество ID видимых по умолчанию колонок
     */
    private static void processColumnHierarchy(List<? extends AbstractColumn> columns, Set<String> locked, Set<String> defaultVisible) {
        if (columns == null) return;
        for (AbstractColumn column : columns) {
            if (column instanceof DndColumn dndColumn)
                processColumnHierarchy(dndColumn.getChildren(), locked, defaultVisible);
            else if (column instanceof BaseColumn baseColumn) {
                boolean enabled = CollectionUtils.isEmpty(locked) || !locked.contains(baseColumn.getId());
                boolean visibleState = CollectionUtils.isEmpty(defaultVisible) || defaultVisible.contains(baseColumn.getId());
                applyStatusToHierarchy(baseColumn, enabled, visibleState);
            }
        }
    }

    /**
     * Применяет статусы блокировки и видимости ко всей иерархии колонок
     */
    private static void applyStatusToHierarchy(BaseColumn column, boolean enabled, boolean visibleState) {
        column.setEnabled(enabled);
        column.setVisibleState(visibleState);
        if (column instanceof MultiColumn multiColumn && multiColumn.getChildren() != null) {
            for (BaseColumn child : multiColumn.getChildren()) {
                applyStatusToHierarchy(child, enabled, visibleState);
            }
        }
    }
}