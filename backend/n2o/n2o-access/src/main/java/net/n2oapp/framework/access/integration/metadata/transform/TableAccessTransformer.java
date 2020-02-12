package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import org.springframework.stereotype.Component;

/**
 * Трансформатор доступа строки таблицы
 */
@Component
public class TableAccessTransformer extends BaseAccessTransformer<Table, CompileContext<?, ?>> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Table.class;
    }

    @Override
    public Table transform(Table compiled, CompileContext context, CompileProcessor p) {
        if (compiled.getComponent() != null && compiled.getComponent().getRowClick() != null) {
            transfer(compiled.getComponent().getRowClick().getAction(), compiled.getComponent().getRows());
        }
        return compiled;
    }
}
