package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.Alignment;
import net.n2oapp.framework.api.metadata.meta.widget.table.ColumnHeader;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.util.StylesResolver;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция абстрактного заголовка таблицы
 */
public abstract class AbstractHeaderCompiler<S extends AbstractColumn> implements BaseSourceCompiler<ColumnHeader, S, CompileContext<?, ?>> {

    protected void compileBaseProperties(S source, ColumnHeader compiled, CompileProcessor p) {
        compiled.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.widget.column.src"), String.class)));
        compiled.setCssClass(source.getCssClass());
        compiled.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        compiled.setAlignment(p.cast(source.getAlignment(), p.resolve(property("n2o.api.widget.column.alignment"), Alignment.class)));
    }
}
