package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCustomCell;
import net.n2oapp.framework.api.metadata.meta.cell.CustomCell;
import org.springframework.stereotype.Component;

/**
 * Компиляция настраиваемой ячейки
 */
@Component
public class CustomCellCompiler extends AbstractCellCompiler<CustomCell, N2oCustomCell> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCustomCell.class;
    }

    @Override
    public CustomCell compile(N2oCustomCell source, CompileContext<?, ?> context, CompileProcessor p) {
        CustomCell cell = new CustomCell();
        build(cell, source, context, p, source.getSrc());
        compileAction(cell, source, context, p);
        return cell;
    }
}
