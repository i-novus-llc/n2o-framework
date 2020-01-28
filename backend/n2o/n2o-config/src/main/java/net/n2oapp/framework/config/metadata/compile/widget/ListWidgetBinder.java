package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.ListWidget;
import org.springframework.stereotype.Component;

/**
 * Связывание данных с списковом виджете
 */
@Component
public class ListWidgetBinder extends BaseListWidgetBinder<ListWidget> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ListWidget.class;
    }

    @Override
    public ListWidget bind(ListWidget compiled, BindProcessor p) {
        bindRowClick(compiled.getRowClick(), p);
        return compiled;
    }
}
