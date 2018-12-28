package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCustomCell;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import org.springframework.stereotype.Component;

/**
 * Компиляция кастомной ячейки
 */
@Component
public class CustomCellCompiler extends AbstractCellCompiler<N2oCustomCell, N2oCustomCell> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCustomCell.class;
    }

    @Override
    public N2oCustomCell compile(N2oCustomCell source, CompileContext<?,?> context, CompileProcessor p) {
        N2oCustomCell cell = new N2oCustomCell();
        build(cell, source, context, p, source.getSrc());
        compileAction(cell, source, context, p);
        return cell;
    }
}
