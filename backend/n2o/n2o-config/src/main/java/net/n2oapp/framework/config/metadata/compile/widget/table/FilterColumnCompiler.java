package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oFilterColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.SimpleColumn;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import org.springframework.stereotype.Component;

/**
 * Компиляция заголовка фильтруемого столбца таблицы
 */
@Component
public class FilterColumnCompiler extends SimpleColumnCompiler<N2oFilterColumn> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oFilterColumn.class;
    }

    @Override
    public SimpleColumn compile(N2oFilterColumn source, CompileContext<?, ?> context, CompileProcessor p) {
        SimpleColumn compiled = super.compile(source, context, p);
        compiled.setFilterable(true);
        compiled.setFilterField(p.compile(source.getFilter(), context, new ComponentScope(source, p.getScope(ComponentScope.class))));
        return compiled;
    }
}
