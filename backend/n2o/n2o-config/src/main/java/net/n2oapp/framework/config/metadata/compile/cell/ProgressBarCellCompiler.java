package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oProgressBarCell;
import net.n2oapp.framework.api.metadata.meta.cell.ProgressBarCell;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция ячейки иконка
 */
@Component
public class ProgressBarCellCompiler extends AbstractCellCompiler<ProgressBarCell, N2oProgressBarCell> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oProgressBarCell.class;
    }

    @Override
    public ProgressBarCell compile(N2oProgressBarCell source, CompileContext<?, ?> context, CompileProcessor p) {
        ProgressBarCell cell = new ProgressBarCell();
        build(cell, source, context, p, property("n2o.api.cell.progress.bar.src"));
        cell.setStriped(p.cast(source.getStriped(),
                p.resolve(property("n2o.api.cell.progress.striped"), Boolean.class)));
        cell.setActive(p.cast(source.getActive(),
                p.resolve(property("n2o.api.cell.progress.active"), Boolean.class)));
        cell.setSize(p.cast(source.getSize(),
                p.resolve(property("n2o.api.cell.progress.size"), N2oProgressBarCell.Size.class)));
        cell.setColor(source.getColor());
        return cell;
    }
}
