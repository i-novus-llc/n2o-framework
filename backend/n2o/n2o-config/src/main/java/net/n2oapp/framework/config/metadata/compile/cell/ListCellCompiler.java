package net.n2oapp.framework.config.metadata.compile.cell;


import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oListCell;
import net.n2oapp.framework.api.metadata.meta.cell.ListCell;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

@Component
public class ListCellCompiler extends AbstractCellCompiler<ListCell, N2oListCell> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oListCell.class;
    }

    @Override
    public ListCell compile(N2oListCell source, CompileContext<?, ?> context, CompileProcessor p) {
        ListCell cell = new ListCell();
        build(cell, source, context, p, property("n2o.api.cell.list.src"));
        cell.setColor(source.getColor() != null ? source.getColor() : compileSwitch(source.getN2oSwitch(), p));
        cell.setSize(source.getSize() != null ? source.getSize() : p.resolve(property("n2o.api.cell.list.size"), Integer.class));
        cell.setLabelFieldId(source.getLabelFieldId());
        return cell;
    }
}
