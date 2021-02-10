package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

@Component
public class SubMenuBinder implements BaseMetadataBinder<Submenu> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Submenu.class;
    }

    @Override
    public Submenu bind(Submenu submenu, BindProcessor p) {
        for (PerformButton item : submenu.getSubMenu()) {
            p.bind(item.getAction());
        }
        return submenu;
    }
}
