package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Базовое связывание виджета с данными
 */
@Component
public class WidgetBinder implements BaseMetadataBinder<Widget<?>> {
    @Override
    public Widget<?> bind(Widget<?> widget, BindProcessor p) {
        if (widget.getToolbar() != null) {
            for (List<Group> grp : widget.getToolbar().values()) {
                grp.forEach(g -> {
                    if (g.getButtons() != null) g.getButtons().forEach(p::bind);
                });
            }
        }
        return widget;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Widget.class;
    }
}
