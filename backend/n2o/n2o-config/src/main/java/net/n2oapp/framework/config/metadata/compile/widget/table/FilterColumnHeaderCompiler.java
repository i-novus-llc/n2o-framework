package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oFilterColumn;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.widget.table.ColumnHeader;
import org.springframework.stereotype.Component;

/**
 * Компиляция заголовка фильтруемого столбца таблицы
 */
@Component
public class FilterColumnHeaderCompiler extends SimpleColumnHeaderCompiler<N2oFilterColumn> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oFilterColumn.class;
    }

    @Override
    public ColumnHeader compile(N2oFilterColumn source, CompileContext<?, ?> context, CompileProcessor p) {
        ColumnHeader header = super.compile(source, context, p);
        header.setFilterable(true);
        StandardField field = p.compile(source.getFilter(), context);
        header.setFilterControl(field.getControl());
        return header;
    }
}
