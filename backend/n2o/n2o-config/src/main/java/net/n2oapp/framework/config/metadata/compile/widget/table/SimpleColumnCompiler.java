package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AlignmentEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oBaseColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.cell.Cell;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.widget.table.SimpleColumn;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.widget.CellsScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static net.n2oapp.framework.api.StringUtils.prepareSizeAttribute;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Компиляция простого заголовка таблицы
 */
@Component
public class SimpleColumnCompiler<S extends N2oSimpleColumn> extends BaseColumnCompiler<S> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSimpleColumn.class;
    }

    @Override
    public SimpleColumn compile(S source, CompileContext<?, ?> context, CompileProcessor p) {
        SimpleColumn compiled = new SimpleColumn();
        compileBaseProperties(source, compiled, p);

        source.setSortingFieldId(castDefault(source.getSortingFieldId(), source.getTextFieldId()));
        source.setAlignment(castDefault(source.getAlignment(),
                () -> p.resolve(property("n2o.api.widget.column.alignment"), AlignmentEnum.class)));
        source.setContentAlignment(castDefault(source.getContentAlignment(), source.getAlignment()));

        N2oCell cell = castDefault(source.getCell(), N2oTextCell::new);

        Cell compiledCell = p.compile(cell, context, new ComponentScope(source), new IndexScope());
        CellsScope cellsScope = p.getScope(CellsScope.class);
        if (cellsScope != null && cellsScope.getCells() != null)
            cellsScope.getCells().add(compiledCell);

        compiled.setIcon(source.getIcon());
        compiled.setResizable(castDefault(source.getResizable(),
                p.resolve(property("n2o.api.widget.table.column.resizable"), Boolean.class)));
        compiled.getElementAttributes().put("width", prepareSizeAttribute(source.getWidth()));
        compiled.setResizable(castDefault(source.getResizable(),
                () -> p.resolve(property("n2o.api.widget.table.column.resizable"), Boolean.class)));
        compiled.setFixed(source.getFixed());
        if (source.getAlignment() != null)
            compiled.getElementAttributes().put("alignment", source.getAlignment().getId());

        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (source.getColumnVisibilities() != null) {
            for (N2oBaseColumn.ColumnVisibility visibility : source.getColumnVisibilities()) {
                String datasourceId = getClientDatasourceId(castDefault(visibility.getDatasourceId(), widgetScope.getDatasourceId()), p);
                ReduxModelEnum refModel = castDefault(visibility.getModel(), ReduxModelEnum.FILTER);
                Condition condition = new Condition();
                condition.setExpression(ScriptProcessor.resolveFunction(visibility.getValue()));
                condition.setModelLink(new ModelLink(refModel, datasourceId).getLink());
                if (!compiled.getConditions().containsKey(ValidationTypeEnum.VISIBLE)) {
                    compiled.getConditions().put(ValidationTypeEnum.VISIBLE, new ArrayList<>());
                }
                compiled.getConditions().get(ValidationTypeEnum.VISIBLE).add(condition);
            }
        }

        CompiledQuery query = p.getScope(CompiledQuery.class);
        compiled.setLabel(initLabel(source, query));
        if (query != null && query.getSimpleFieldsMap().containsKey(source.getSortingFieldId())) {
            boolean sortable = query.getSimpleFieldsMap().get(source.getSortingFieldId()).getIsSorted();
            if (sortable) {
                compiled.setSortingParam(RouteUtil.normalizeParam(source.getSortingFieldId()));
            }
        }

        compiled.setProperties(p.mapAttributes(source));

        return compiled;
    }

    private String initLabel(S source, CompiledQuery query) {
        if (source.getLabel() != null)
            return source.getLabel();
        if (query != null && query.getSimpleFieldsMap().containsKey(source.getTextFieldId()))
            return query.getSimpleFieldsMap().get(source.getTextFieldId()).getName();
        return source.getTextFieldId();
    }
}
