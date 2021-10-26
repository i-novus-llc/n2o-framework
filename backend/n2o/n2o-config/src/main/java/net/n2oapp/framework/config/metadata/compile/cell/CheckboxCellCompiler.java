package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCheckboxCell;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

@Component
public class CheckboxCellCompiler extends AbstractCellCompiler<N2oCheckboxCell, N2oCheckboxCell> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCheckboxCell.class;
    }

    @Override
    public N2oCheckboxCell compile(N2oCheckboxCell source, CompileContext<?, ?> context, CompileProcessor p) {
        N2oCheckboxCell cell = new N2oCheckboxCell();
        if (source.getEnabled() != null) {
            cell.setEnabled(source.getEnabled());
        } else if (source.getActionId() == null && source.getN2oAction() == null) {
            cell.setEnabled("false");
        }

        build(cell, source, context, p, property("n2o.api.cell.checkbox.src"));
        compileAction(cell, source, context, p);
        return cell;
    }
}
