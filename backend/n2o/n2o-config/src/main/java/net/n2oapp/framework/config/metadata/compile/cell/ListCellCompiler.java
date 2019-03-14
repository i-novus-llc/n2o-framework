package net.n2oapp.framework.config.metadata.compile.cell;


import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oListCell;
import net.n2oapp.framework.api.script.ScriptProcessor;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

@Component
public class ListCellCompiler extends AbstractCellCompiler<N2oListCell, N2oListCell> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oListCell.class;
    }

    @Override
    public N2oListCell compile(N2oListCell source, CompileContext<?, ?> context, CompileProcessor p) {
        N2oListCell cell = new N2oListCell();
        build(cell, source, context, p, property("n2o.default.cell.list.src"));
        cell.setColor(source.getColor() != null ? source.getColor() : compileSwitch(source.getN2oSwitch(), p));
        cell.setSize(source.getSize() != null ? source.getSize() : p.resolve(property("n2o.default.cell.list.size"), Integer.class));
        cell.setLabelFieldId(source.getLabelFieldId());
        return cell;
    }
}
