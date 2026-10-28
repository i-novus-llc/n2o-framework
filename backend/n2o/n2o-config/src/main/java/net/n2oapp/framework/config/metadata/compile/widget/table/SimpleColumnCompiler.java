package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AlignmentEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.cell.Cell;
import net.n2oapp.framework.api.metadata.meta.widget.table.SimpleColumn;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.widget.CellsScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.StringUtils.prepareSizeAttribute;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

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
        if (source.getAlignment() != null)
            compiled.getElementAttributes().put("alignment", source.getAlignment().getId());

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