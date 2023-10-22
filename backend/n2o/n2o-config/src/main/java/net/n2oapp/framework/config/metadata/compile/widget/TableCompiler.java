package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oSearchButtons;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetColumn;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetRow;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.*;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.control.SearchButtons;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
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
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.StringUtils.prepareSizeAttribute;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
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
                ReduxModel.filter, p
        );
        SubModelsScope subModelsScope = p.cast(p.getScope(SubModelsScope.class), SubModelsScope::new);
        ValidationScope validationScope = p.getScope(ValidationScope.class) == null ? new ValidationScope() : p.getScope(ValidationScope.class);
        FiltersScope filtersScope = p.getScope(FiltersScope.class);
        TableFiltersScope tableFiltersScope = null;
        if (filtersScope != null) {
            tableFiltersScope = new TableFiltersScope(datasource.getId(), filtersScope);
        }

        initFilter(
                table, source, context, p, widgetScope, query, object,
                new ModelsScope(ReduxModel.filter, widgetScope.getClientDatasourceId(), p.getScope(Models.class)),
                subModelsScope,
                new MomentScope(N2oValidation.ServerMoment.beforeQuery),
                validationScope,
                tableFiltersScope
        );
        MetaActions widgetActions = initMetaActions(source, p);
        TableWidgetComponent component = table.getComponent();
        compileToolbarAndAction(
                table, source, context, p, widgetScope, widgetActions, object, null
        );
        compileColumns(
                source, context, p, component, query, object, widgetScope, widgetActions, subModelsScope, tableFiltersScope
        );
        component.setWidth(prepareSizeAttribute(source.getWidth()));
        component.setHeight(prepareSizeAttribute(source.getHeight()));
        component.setTextWrap(
                p.cast(source.getTextWrap(), p.resolve(property("n2o.api.widget.table.text_wrap"), Boolean.class))
        );
        if (source.getRows() != null) {
            if (component.getBody().getRow() == null) {
                component.getBody().setRow(new TableWidgetComponent.BodyRow());
            }
            component.getBody().getRow().setSrc(source.getRows().getSrc());
            if (source.getRows().getStyle() != null) {
                component.getBody().getRow().getElementAttributes().put("style", p.resolveJS(source.getRows().getStyle()));
            }
            if (source.getRows().getRowClass() != null) {
                component.getBody().getRow().getElementAttributes().put("className", p.resolveJS(source.getRows().getRowClass()));
            } else {
                if (source.getRows().getColor() != null) {
                    Map<Object, String> resolvedCases = new HashMap<>();
                    for (String key : source.getRows().getColor().getCases().keySet()) {
                        resolvedCases.put(
                                p.resolve(key), source.getRows().getColor().getCases().get(key)
                        );
                    }
                    source.getRows().getColor().setResolvedCases(resolvedCases);
                    component.getBody()
                            .getRow()
                            .getElementAttributes()
                            .put("className", buildSwitchExpression(source.getRows().getColor()));
                }
            }
            component.getBody().getRow().setClick(compileRowClick(source, context, p, widgetScope, object, widgetActions));
            PageIndexScope pageIndexScope = new PageIndexScope(source.getId());
            component.getBody().getRow().setOverlay(compileRowOverlay(source, context, p, widgetScope, object, pageIndexScope));

        }
        table.setPaging(
                compilePaging(table, source, p.resolve(property("n2o.api.widget.table.size"), Integer.class), p)
        );
        table.setChildren(
                p.cast(source.getChildren(), () -> p.resolve(property("n2o.api.widget.table.children.toggle"), ChildrenToggle.class))
        );
        component.setAutoSelect(
                p.cast(source.getAutoSelect(), () -> p.resolve(property("n2o.api.widget.table.auto_select"), Boolean.class))
        );

        return table;
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
        if (source.getColumns() != null) {
            List<ColumnHeader> headers = new ArrayList<>();
            Map<String, String> sortings = new HashMap<>();
            IndexScope columnIndex = new IndexScope();
            CellsScope cellsScope = new CellsScope(new ArrayList<>());

            for (AbstractColumn column : source.getColumns()) {
                headers.add(
                        p.compile(column, context, p, new ComponentScope(column), object, columnIndex, cellsScope, query, scopes)
                );
                if (column.getSortingDirection() != null) {
                    String fieldId = p.cast(column.getSortingFieldId(), column.getTextFieldId());
                    if (fieldId == null)
                        throw new N2oException(String.format("В колонке <column> c id=%s задан атрибут 'sorting-direction', но не указано поле сортировки. Задайте 'sorting-field-id' или 'text-field-id'",
                                column.getId()));
                    sortings.put(RouteUtil.normalizeParam(fieldId),
                            column.getSortingDirection().toString().toUpperCase()
                    );
                }
            }
            component.getHeader().setCells(headers);
            component.getBody().setCells(cellsScope.getCells());
            if (isNotEmpty(sortings)) {
                passSortingToDatasource(sortings, source, p);
            }
            component.setRowSelection(p.cast(source.getSelection(), () -> p.resolve(property("n2o.api.widget.table.selection"), RowSelectionEnum.class)));
        }
    }

    private void passSortingToDatasource(Map<String, String> sortings, N2oTable source, CompileProcessor p) {
        String sourceDatasourceId = p.getScope(PageScope.class)
                .getWidgetIdSourceDatasourceMap()
                .get(source.getId());

        p.getScope(DataSourcesScope.class)
                .get(sourceDatasourceId)
                .setSorting(sortings);
    }

    private void initFilter(Table compiled, N2oTable source, CompileContext<?, ?> context, CompileProcessor p,
                            WidgetScope widgetScope, CompiledQuery widgetQuery, CompiledObject object,
                            Object... scopes) {
        List<FieldSet> fieldSets = initFieldSets(source.getFilters() == null ? null : source.getFilters().getItems(), context, p, widgetScope,
                widgetQuery, object, scopes);
        if (fieldSets.isEmpty())
            return;
        AbstractTable.Filter filter = new AbstractTable.Filter();
        filter.setFilterFieldsets(fieldSets);
        filter.setFilterButtonId("filter");
        List<N2oSearchButtons> searchButtons = new ArrayList<>();
        findSearchButton(
                source.getFilters().getItems(),
                searchButtons
        );
        filter.setBlackResetList(
                new ArrayList<>(
                        searchButtons.stream()
                                .filter(f -> f.getClearIgnore() != null)
                                .flatMap(f -> Arrays.stream(f.getClearIgnore().split(",")))
                                .map(String::trim)
                                .collect(Collectors.toSet())
                )
        );
        filter.setFilterPlace(p.cast(source.getFilters().getPlace(), FilterPosition.TOP));
        boolean hasSearchButtons = fieldSets.stream()
                .flatMap(fs -> fs.getRows() != null ? fs.getRows().stream() : Stream.empty())
                .flatMap(r -> r.getCols() != null ? r.getCols().stream() : Stream.empty())
                .flatMap(c -> c.getFields() != null ? c.getFields().stream() : Stream.empty())
                .filter(StandardField.class::isInstance)
                .map(f -> ((StandardField<?>) f).getControl())
                .anyMatch(SearchButtons.class::isInstance);
        filter.setSearchOnChange(source.getFilters().getSearchOnChange());
        if (hasSearchButtons || (filter.getSearchOnChange() != null && filter.getSearchOnChange())) {
            filter.setHideButtons(true);
        }
        initInlineFiltersDatasource(compiled, source, p);
        compiled.setFilter(filter);
    }

    private void findSearchButton(SourceComponent[] filters, List<N2oSearchButtons> searchButtons) {
        if (filters == null)
            return;
        for (SourceComponent f : filters) {
            if (f instanceof N2oSearchButtons) {
                searchButtons.add((N2oSearchButtons) f);
            } else if (f instanceof N2oFieldsetRow) {
                findSearchButton(((N2oFieldsetRow) f).getItems(), searchButtons);
            } else if (f instanceof N2oFieldsetColumn) {
                findSearchButton(((N2oFieldsetColumn) f).getItems(), searchButtons);
            }
        }
    }

    /**
     * Инициализация встроенного источника данных
     */
    protected void initInlineFiltersDatasource(Table compiled, N2oTable source, CompileProcessor p) {
        if (source.getFilters() == null || source.getFilters().getDatasourceId() == null && source.getFilters().getDatasource() == null)
            return;
        String datasourceId = source.getFilters().getDatasourceId();
        if (datasourceId == null) {
            datasourceId = source.getId() + "_filter";
            N2oStandardDatasource datasource = source.getFilters().getDatasource();
            source.getFilters().setDatasource(null);
            datasource.setId(datasourceId);
            source.getFilters().setDatasourceId(datasourceId);
            DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
            if (dataSourcesScope != null) {
                dataSourcesScope.put(datasourceId, datasource);
            }
        }
        compiled.setFiltersDatasourceId(getClientDatasourceId(datasourceId, p));
    }

    /**
     * Компиляция поведения при наведении на строку
     */
    protected RowOverlay compileRowOverlay(N2oAbstractListWidget source, CompileContext<?, ?> context,
                                           CompileProcessor p, WidgetScope widgetScope,
                                           CompiledObject object, PageIndexScope indexScope) {
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

}