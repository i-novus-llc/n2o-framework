package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import org.apache.commons.collections.CollectionUtils;
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
        if (compiled.getFilter() != null && CollectionUtils.isNotEmpty(compiled.getFilter().getFilterFieldsets())) {
            compiled.getFilter().getFilterFieldsets().forEach(p::bind);
        }
        if (compiled.getComponent().getBody().getRow() != null && compiled.getComponent().getBody().getRow().getClick() != null) {
            bindRowClick(compiled.getComponent().getBody().getRow().getClick(), p);
        }
        if (compiled.getComponent().getBody().getCells() != null)
            compiled.getComponent().getBody().getCells().forEach(p::bind);
        return compiled;
    }
}
