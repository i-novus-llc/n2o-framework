package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oAbstractColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.AbstractColumn;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция абстрактного заголовка таблицы
 */
public abstract class AbstractColumnCompiler<S extends N2oAbstractColumn> implements BaseSourceCompiler<AbstractColumn, S, CompileContext<?, ?>> {

    protected void compileAbstractProperties(S source, AbstractColumn compiled, CompileProcessor p) {
        compiled.setSrc(castDefault(source.getSrc(), () -> p.resolve(property("n2o.api.widget.column.src"), String.class)));
    }
}
