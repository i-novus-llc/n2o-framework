package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание данных в таблице
 */
@Component
public class TableBinder implements BaseMetadataBinder<Table> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Table.class;
    }

    @Override
    public Table bind(Table compiled, BindProcessor p) {
        if (compiled.getComponent().getRowClick() != null)
            p.bind(compiled.getComponent().getRowClick());
        return compiled;
    }
}
