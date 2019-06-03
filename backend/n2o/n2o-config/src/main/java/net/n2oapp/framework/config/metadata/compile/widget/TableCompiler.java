package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oRowClick;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.table.AbstractTable;
import net.n2oapp.framework.api.metadata.meta.widget.table.ColumnHeader;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.table.TableWidgetComponent;
import net.n2oapp.framework.config.metadata.compile.*;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import org.springframework.stereotype.Component;

import java.util.*;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.script.ScriptProcessor.buildSwitchExpression;


/**
 * Компиляция таблицы
 */
@Component
public class TableCompiler extends BaseWidgetCompiler<Table, N2oTable> {

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
        CompiledQuery query = getQuery(source, p);
        CompiledObject object = getObject(source, p);
        compileWidget(table, source, context, p, object);
        WidgetScope widgetScope = new WidgetScope();
        widgetScope.setClientWidgetId(table.getId());
        widgetScope.setWidgetId(source.getId());
        widgetScope.setQueryId(source.getQueryId());
        Models models = p.getScope(Models.class);
        SubModelsScope subModelsScope = new SubModelsScope();
        UploadScope uploadScope = new UploadScope();
        uploadScope.setUpload(UploadType.defaults);
        table.setFilter(createFilter(source, context, p, widgetScope, query, object,
                new ModelsScope(ReduxModel.FILTER, table.getId(), models), new FiltersScope(table.getFilters()), subModelsScope, uploadScope,
                new MomentScope(N2oValidation.ServerMoment.beforeQuery)));
        ValidationList validationList = p.getScope(ValidationList.class) == null ? new ValidationList(new HashMap<>()) : p.getScope(ValidationList.class);
        ValidationScope validationScope = new ValidationScope(table.getId(), ReduxModel.FILTER, validationList);
        //порядок вызова compileValidation и compileDataProviderAndRoutes важен
        compileValidation(table, source, validationScope);
        ParentRouteScope widgetRouteScope = initWidgetRouteScope(table, context, p);
        PageRoutesScope pageRoutesScope = p.getScope(PageRoutesScope.class);
        if (pageRoutesScope != null) {
            pageRoutesScope.put(table.getId(), widgetRouteScope);
        }
        compileDataProviderAndRoutes(table, source, p, validationList, widgetRouteScope, null, null);
        component.setSize(source.getSize() != null ? source.getSize() : p.resolve("${n2o.api.default.widget.table.size}", Integer.class));
        component.setClassName(source.getCssClass());
        MetaActions widgetActions = new MetaActions();
        compileToolbarAndAction(table, source, context, p, widgetScope, widgetRouteScope, widgetActions, object, null);
        if (source.getRows() != null) {
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
            compileRowClick(source, component, context, p, widgetScope, widgetRouteScope);
        }
        compileColumns(source, context, p, component, query, object, widgetScope, widgetRouteScope, widgetActions);
        Boolean prev = null;
        Boolean next = null;
        if (source.getPagination() != null) {
            prev = source.getPagination().getPrev();
            next = source.getPagination().getNext();
        }
        table.setPaging(createPaging(source.getSize(), prev, next, "n2o.api.default.widget.table.size", p));
        return table;
    }

    private void compileRowClick(N2oTable source, TableWidgetComponent component, CompileContext<?, ?> context,
                                 CompileProcessor p, WidgetScope widgetScope, ParentRouteScope widgetRouteScope) {
        N2oRowClick rowClick = source.getRows().getRowClick();
        if (rowClick != null) {
            if (rowClick.getActionId() != null) {
                MetaActions actions = p.getScope(MetaActions.class);
                Action action = actions.get(rowClick.getActionId());
                component.setRowClick(action);
            } else if (rowClick.getAction() != null) {
                Action action = p.compile(rowClick.getAction(), context, widgetScope,
                        widgetRouteScope, new ComponentScope(rowClick));
                component.setRowClick(action);
            }
        }
    }

    @Override
    protected QueryContext getQueryContext(Table widget, N2oTable source, String route, CompiledQuery query,
                                           ValidationList validationList, SubModelsScope subModelsScope,
                                           CopiedFieldScope copiedFieldScope, CompileProcessor p) {
        QueryContext queryContext = super.getQueryContext(widget, source, route, query, validationList, subModelsScope, copiedFieldScope, p);

        queryContext.setSortingMap(new StrictMap<>());
        if (source.getColumns() != null) {
            for (AbstractColumn column : source.getColumns()) {
                String id = column.getId() != null ? column.getId() : column.getTextFieldId();
                String sortingFieldId = column.getSortingFieldId() != null ? column.getSortingFieldId() : column.getTextFieldId();
                queryContext.getSortingMap().put(id, sortingFieldId);
            }
        }
        return queryContext;
    }

    @Override
    protected String getMessagesForm(Widget widget) {
        return widget.getId() + "_filter";
    }

    private void compileValidation(Table table, N2oTable source, ValidationScope validationScope) {
        if (source.getFilters() == null)
            return;
        Map<String, List<Validation>> clientValidations = new HashMap<>();
        table.getFilter().getFilterFieldsets().forEach(fs -> collectValidation(fs, clientValidations, validationScope));
        table.getFilter().setValidation(clientValidations);
    }

    private void compileColumns(N2oTable source, CompileContext<?, ?> context, CompileProcessor p,
                                TableWidgetComponent component, CompiledQuery query, CompiledObject object,
                                WidgetScope widgetScope, ParentRouteScope widgetRouteScope, MetaActions widgetActions) {
        if (source.getColumns() != null) {
            List<ColumnHeader> headers = new ArrayList<>();
            List<N2oCell> cells = new ArrayList<>();
            Map<String, String> sortings = new HashMap<>();
            IndexScope columnIndex = new IndexScope();
            for (AbstractColumn column : source.getColumns()) {
                compileHeaderWithCell(object, query, headers, cells, column, context, p, columnIndex, widgetScope, widgetRouteScope, widgetActions);
                if (column.getSortingDirection() != null) {
                    sortings.put(column.getTextFieldId(), column.getSortingDirection().toString().toUpperCase());
                }
            }
            component.setHeaders(headers);
            component.setCells(cells);
            component.setSorting(sortings);
            Boolean hasSelect = p.cast(source.getSelected(), p.resolve(property("n2o.api.widget.table.selected"), Boolean.class));
            component.setHasSelect(hasSelect);
            component.setHasFocus(hasSelect);
        }
    }

    private void compileHeaderWithCell(CompiledObject object, CompiledQuery query, List<ColumnHeader> headers, List<N2oCell> cells,
                                       AbstractColumn column,
                                       CompileContext<?, ?> context, CompileProcessor p,
                                       IndexScope columnIndex, WidgetScope widgetScope, ParentRouteScope widgetRouteScope, MetaActions widgetActions) {
        ColumnHeader header = new ColumnHeader();
        column.setId(p.cast(column.getId(), column.getTextFieldId()));
        column.setSortingFieldId(p.cast(column.getSortingFieldId(), column.getTextFieldId()));
        header.setId(column.getId());
        header.setWidth(column.getWidth());
        header.setIcon(column.getLabelIcon());
        if (query != null && query.getFieldsMap().containsKey(column.getTextFieldId())) {
            header.setLabel(p.cast(column.getLabelName(), query.getFieldsMap().get(column.getTextFieldId()).getName()));
        } else {
            header.setLabel(column.getLabelName());
        }
        if (query != null && query.getFieldsMap().containsKey(header.getId())) {
            header.setSortable(!query.getFieldsMap().get(header.getId()).getNoSorting());
        }
        headers.add(header);
        if (column instanceof N2oSimpleColumn) {
            N2oCell cell = ((N2oSimpleColumn) column).getCell();
            if (cell == null) {
                cell = new N2oTextCell();
            }
            cell = p.compile(cell, context, columnIndex, widgetScope, widgetRouteScope, new ComponentScope(column), object, widgetActions);
            cells.add(cell);
        }
    }

    private AbstractTable.Filter createFilter(N2oTable source, CompileContext<?, ?> context, CompileProcessor p,
                                              WidgetScope widgetScope, CompiledQuery widgetQuery, CompiledObject object,
                                              ModelsScope modelsScope, FiltersScope filtersScope,
                                              SubModelsScope subModelsScope, UploadScope uploadScope, MomentScope momentScope) {
        List<FieldSet> fieldSets = initFieldSets(source.getFilters(), context, p, widgetScope,
                widgetQuery, object, modelsScope, filtersScope, subModelsScope, uploadScope, momentScope, null);
        if (fieldSets.isEmpty())
            return null;
        AbstractTable.Filter filter = new AbstractTable.Filter();
        filter.setFilterFieldsets(fieldSets);
        filter.setFilterButtonId("filter");
        filter.setBlackResetList(Collections.EMPTY_LIST);
        filter.setFilterPlace(p.cast(source.getFilterPosition(), N2oTable.FilterPosition.top));
        filter.setHideButtons(p.cast(source.getSearchButtons(), true) ? null : true);
        return filter;
    }

}

