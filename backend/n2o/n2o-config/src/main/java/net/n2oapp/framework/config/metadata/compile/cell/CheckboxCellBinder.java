package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCheckboxCell;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

@Component
public class CheckboxCellBinder implements BaseMetadataBinder<N2oCheckboxCell> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return N2oCheckboxCell.class;
    }

    @Override
    public N2oCheckboxCell bind(N2oCheckboxCell compiled, BindProcessor p) {
        if (compiled.getCompiledAction() != null)
            p.bind(compiled.getCompiledAction());
        return compiled;
    }
}
