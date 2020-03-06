package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.ColumnHeader;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;

/**
 * Компиляция базового хэдера
 */
public abstract class BaseHeaderCompiler<S extends AbstractColumn> implements BaseSourceCompiler<ColumnHeader, S, CompileContext<?, ?>> {

    protected ColumnHeader compileHeader(S column, CompileProcessor p) {
        ColumnHeader header = new ColumnHeader();
        header.setId(column.getId());
        header.setIcon(column.getLabelIcon());
        header.setWidth(column.getWidth());
        header.setResizable(column.getResizable());
        header.setFixed(column.getFixed());
        return header;
    }
}
