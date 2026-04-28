package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.table.DndColumn;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание данных в drag-n-drop столбце таблицы
 */
@Component
public class DndColumnBinder implements BaseMetadataBinder<DndColumn> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return DndColumn.class;
    }

    @Override
    public DndColumn bind(DndColumn compiled, BindProcessor p) {
        if (compiled.getChildren() != null) {
            compiled.getChildren().forEach(p::bind);
        }
        return compiled;
    }
}
