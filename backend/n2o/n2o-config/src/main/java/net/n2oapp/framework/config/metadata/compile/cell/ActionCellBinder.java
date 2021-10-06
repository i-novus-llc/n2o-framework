package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oActionCell;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание с данными любых ячеек с действием
 */
@Component
public class ActionCellBinder implements BaseMetadataBinder<N2oActionCell> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return N2oActionCell.class;
    }

    @Override
    public N2oActionCell bind(N2oActionCell compiled, BindProcessor p) {
        if (compiled.getCompiledAction() != null)
            p.bind(compiled.getCompiledAction());
        return compiled;
    }
}