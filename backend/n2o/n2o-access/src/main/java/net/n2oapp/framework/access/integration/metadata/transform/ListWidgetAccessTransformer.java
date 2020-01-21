package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.ListWidget;
import org.springframework.stereotype.Component;

/**
 * Трансформатор доступа строки списка
 */
@Component
public class ListWidgetAccessTransformer extends BaseAccessTransformer<ListWidget, CompileContext<?, ?>> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ListWidget.class;
    }

    @Override
    public ListWidget transform(ListWidget compiled, CompileContext<?, ?> context, CompileProcessor p) {
        if (compiled.getRows() != null && compiled.getRowClick().getAction() != null) {
            transfer(compiled.getRowClick().getAction(), compiled.getRows());
        }
        return compiled;
    }
}
