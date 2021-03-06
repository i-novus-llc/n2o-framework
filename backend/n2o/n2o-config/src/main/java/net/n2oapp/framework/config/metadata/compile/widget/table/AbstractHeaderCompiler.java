package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.ColumnHeader;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;

/**
 * Компиляция абстрактного заголовка таблицы
 */
public abstract class AbstractHeaderCompiler<S extends AbstractColumn> implements BaseSourceCompiler<ColumnHeader, S, CompileContext<?, ?>> {

}
