package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oSearchButtons;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.RowSelectionEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.control.SearchButtons;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.widget.Rows;
import net.n2oapp.framework.api.metadata.meta.widget.table.AbstractTable;
import net.n2oapp.framework.api.metadata.meta.widget.table.ColumnHeader;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.table.TableWidgetComponent;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.ValidationList;
import net.n2oapp.framework.config.metadata.compile.ValidationScope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.script.ScriptProcessor.buildSwitchExpression;

/**
 * Компиляция таблицы
 */
@Component
public class TableCompiler extends BaseListWidgetCompiler<Table, N2oTable> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTable.class;
    }

    @Override
    protected String getPropertyWidgetSrc() {
        return "n2o.api.widget.table.src";
    }

    @Override
    public Table compile(N2oTable source, CompileContext<?, ?> context, CompileProcessor p) {
        Table table = new Table();
        TableWidgetComponent component = table.getComponent();
        N2oDatasource datasource = initInlineDatasource(table, source, p);
        CompiledQuery query = getQuery(source, datasource, p);
        CompiledObject object = getObject(source, datasource, p);
        compileBaseWidget(table, source, context, p, object);
        WidgetScope widgetScope = new WidgetScope();
        widgetScope.setClientWidgetId(table.getId());
        widgetScope.setWidgetId(source.getId());
        widgetScope.setDatasourceId(source.getDatasourceId());
        SubModelsScope subModelsScope = new SubModelsScope();
        ValidationList validationList = p.getScope(ValidationList.class) == null ? new ValidationList() : p.getScope(ValidationList.class);
        ValidationScope validationScope = new ValidationScope(datasource, ReduxModel.filter, validationList);
        table.setFilter(createFilter(source, context, p, widgetScope, query, object,
                new ModelsScope(ReduxModel.filter, table.getId(), p.getScope(Models.class)), subModelsScope,
                new MomentScope(N2oValidation.ServerMoment.beforeQuery), validationScope));
        MetaActions widgetActions = initMetaActions(source);
        compileToolbarAndAction(table, source, context, p, widgetScope, widgetActions, object, null);
        compileColumns(source, context, p, component, query, object, widgetScope, widgetActions,
                subModelsScope);
        component.setSize(p.cast(source.getSize(), p.resolve(property("n2o.api.widget.table.size"), Integer.class)));
        component.setTableSize(source.getTableSize() != null ? source.getTableSize().name().toLowerCase() : null);
        component.setWidth(source.getWidth());
        component.setHeight(source.getHeight());
        component.setTextWrap(p.cast(source.getTextWrap(), p.resolve(property("n2o.api.widget.table.text_wrap"), Boolean.class)));
        if (source.getRows() != null) {
            component.setRows(new Rows());
            if (source.getRows().getRowClass() != null) {
                component.setRowClass(p.resolveJS(source.getRows().getRowClass()));
            } else {
                if (source.getRows().getColor() != null) {
                    Map<Object, String> resolvedCases = new HashMap<>();
                    for (String key : source.getRows().getColor().getCases().keySet()) {
                        resolvedCases.put(p.resolve(key), source.getRows().getColor().getCases().get(key));
                    }
                    source.getRows().getColor().setResolvedCases(resolvedCases);
                    component.setRowClass(buildSwitchExpression(source.getRows().getColor()));
                }
            }
            component.setRowClick(compileRowClick(source, context, p, widgetScope, object, widgetActions));
        }
        table.setPaging(compilePaging(table, source, p.resolve(property("n2o.api.widget.table.size"), Integer.class), p));
        table.setChildren(p.cast(source.getChildren(),
                p.resolve(property("n2o.api.widget.table.children.toggle"), N2oTable.ChildrenToggle.class))
        );
        component.setAutoCheckboxOnSelect(p.cast(source.getCheckOnSelect(), p.resolve(property("n2o.api.widget.table.check_on_select"), Boolean.class)));
        if (Boolean.TRUE.equals(source.getCheckboxes()))
            component.setRowSelection(RowSelectionEnum.checkbox);
        return table;
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
                headers.add(p.compile(column, context, p, new ComponentScope(column), object, columnIndex, cellsScope, query, scopes));
                if (column.getSortingDirection() != null) {
                    sortings.put(column.getTextFieldId(), column.getSortingDirection().toString().toUpperCase());
                }
            }
            component.setHeaders(headers);
            component.setCells(cellsScope.getCells());
            component.setSorting(sortings);

            RowSelectionEnum rowSelection = p.cast(source.getSelection(), p.resolve(property("n2o.api.widget.table.selection"), RowSelectionEnum.class));
            switch (rowSelection) {
                case none:
                    component.setHasSelect(false);
                    component.setHasFocus(false);
                    break;
                case active:
                    component.setHasSelect(true);
                    component.setHasFocus(true);
                    break;
                case radio:
                case checkbox:
                    component.setRowSelection(rowSelection);
                    break;
            }
        }
    }

    private AbstractTable.Filter createFilter(N2oTable source, CompileContext<?, ?> context, CompileProcessor p,
                                              WidgetScope widgetScope, CompiledQuery widgetQuery, CompiledObject object,
                                              Object... scopes) {
        List<FieldSet> fieldSets = initFieldSets(source.getFilters(), context, p, widgetScope,
                widgetQuery, object, scopes);

        if (fieldSets.isEmpty())
            return null;
        AbstractTable.Filter filter = new AbstractTable.Filter();
        filter.setFilterFieldsets(fieldSets);
        filter.setFilterButtonId("filter");
        filter.setBlackResetList(new ArrayList<>(Arrays.stream(source.getFilters())
                .filter(f -> f instanceof N2oSearchButtons && ((N2oSearchButtons) f).getClearIgnore() != null)
                .flatMap(f -> Arrays.stream(((N2oSearchButtons) f).getClearIgnore().split(",")))
                .map(String::trim)
                .collect(Collectors.toSet())
        ));
        filter.setFilterPlace(p.cast(source.getFilterPosition(), N2oTable.FilterPosition.top));
        boolean hasSearchButtons = fieldSets.stream()
                .flatMap(fs -> fs.getRows() != null ? fs.getRows().stream() : Stream.empty())
                .flatMap(r -> r.getCols() != null ? r.getCols().stream() : Stream.empty())
                .flatMap(c -> c.getFields() != null ? c.getFields().stream() : Stream.empty())
                .filter(f -> f instanceof StandardField)
                .map(f -> ((StandardField<?>) f).getControl())
                .anyMatch(c -> c instanceof SearchButtons);
        filter.setSearchOnChange(source.getSearchOnChange());
        if (hasSearchButtons || (filter.getSearchOnChange() != null && filter.getSearchOnChange()))
            filter.setHideButtons(true);
        return filter;
    }
}

