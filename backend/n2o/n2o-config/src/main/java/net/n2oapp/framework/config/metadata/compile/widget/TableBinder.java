package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import org.springframework.stereotype.Component;

/**
 * Связывание данных в таблице
 */
@Component
public class TableBinder extends BaseListWidgetBinder<Table> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Table.class;
    }

    @Override
    public Table bind(Table compiled, BindProcessor p) {
        bindRowClick(compiled.getComponent().getRowClick(), p);
        if (compiled.getComponent().getCells() != null)
            compiled.getComponent().getCells().forEach(p::bind);
        return compiled;
    }
}
