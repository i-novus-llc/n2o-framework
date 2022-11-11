package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.Alignment;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.cell.Cell;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.widget.table.ColumnHeader;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.widget.CellsScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static net.n2oapp.framework.api.StringUtils.isLink;
import static net.n2oapp.framework.api.StringUtils.unwrapLink;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Компиляция простого заголовка таблицы
 */
@Component
public class SimpleColumnHeaderCompiler<T extends N2oSimpleColumn> extends AbstractHeaderCompiler<T> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSimpleColumn.class;
    }

    @Override
    public ColumnHeader compile(T source, CompileContext<?, ?> context, CompileProcessor p) {
        ColumnHeader header = new ColumnHeader();
        IndexScope idx = p.getScope(IndexScope.class);
        int indexNumber = idx.get();

        source.setId(p.cast(source.getId(), source.getTextFieldId(), "cell" + indexNumber));
        source.setSortingFieldId(p.cast(source.getSortingFieldId(), source.getTextFieldId()));
        source.setAlignment(p.cast(source.getAlignment(),
                p.resolve(property("n2o.api.widget.column.alignment"), Alignment.class)));
        source.setContentAlignment(p.cast(source.getContentAlignment(), source.getAlignment()));

        N2oCell cell = source.getCell();
        if (cell == null) {
            cell = new N2oTextCell();
        }
        Cell compiledCell = p.compile(cell, context, new ComponentScope(source), new IndexScope());
        CellsScope cellsScope = p.getScope(CellsScope.class);
        if (cellsScope != null && cellsScope.getCells() != null)
            cellsScope.getCells().add(compiledCell);

        compileBaseProperties(source, header, p);
        header.setId(source.getId());
        header.setIcon(source.getLabelIcon());
        header.setWidth(source.getWidth());
        header.setResizable(source.getResizable());
        header.setFixed(source.getFixed());
        header.setAlignment(source.getAlignment());

        WidgetScope widgetScope = p.getScope(WidgetScope.class);

        if (isLink(source.getVisible())) {
            Condition condition = new Condition();
            condition.setExpression(unwrapLink(source.getVisible()));
            String datasourceId = widgetScope.getClientDatasourceId();
            condition.setModelLink(new ModelLink(ReduxModel.filter, datasourceId).getBindLink());
            if (!header.getConditions().containsKey(ValidationType.visible)) {
                header.getConditions().put(ValidationType.visible, new ArrayList<>());
            }
            header.getConditions().get(ValidationType.visible).add(condition);
        } else {
            header.setVisible(p.resolveJS(source.getVisible(), Boolean.class));
        }
        if (source.getColumnVisibilities() != null) {
            for (AbstractColumn.ColumnVisibility visibility : source.getColumnVisibilities()) {
                String datasourceId = getClientDatasourceId(p.cast(visibility.getDatasourceId(), widgetScope.getDatasourceId()), p);
                ReduxModel refModel = p.cast(visibility.getModel(), ReduxModel.filter);
                Condition condition = new Condition();
                condition.setExpression(ScriptProcessor.resolveFunction(visibility.getValue()));
                condition.setModelLink(new ModelLink(refModel, datasourceId).getBindLink());
                if (!header.getConditions().containsKey(ValidationType.visible)) {
                    header.getConditions().put(ValidationType.visible, new ArrayList<>());
                }
                header.getConditions().get(ValidationType.visible).add(condition);
            }
        }

        CompiledQuery query = p.getScope(CompiledQuery.class);
        header.setLabel(initLabel(source, query));
        if (query != null && query.getSimpleFieldsMap().containsKey(source.getSortingFieldId())) {
            boolean sortable = query.getSimpleFieldsMap().get(source.getSortingFieldId()).getIsSorted();
            if (sortable) {
                header.setSortingParam(RouteUtil.normalizeParam(source.getSortingFieldId()));
            }
        }

        header.setProperties(p.mapAttributes(source));

        return header;
    }

    private String initLabel(T source, CompiledQuery query) {
        if (source.getLabelName() != null)
            return source.getLabelName();
        if (query != null && query.getSimpleFieldsMap().containsKey(source.getTextFieldId()))
            return query.getSimpleFieldsMap().get(source.getTextFieldId()).getName();
        return source.getTextFieldId();
    }
}
