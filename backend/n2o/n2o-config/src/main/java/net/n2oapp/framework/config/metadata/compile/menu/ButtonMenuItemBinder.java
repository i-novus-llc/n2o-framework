package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeUtil;
import net.n2oapp.framework.api.metadata.meta.menu.ButtonMenuItem;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

@Component
public class ButtonMenuItemBinder implements BaseMetadataBinder<ButtonMenuItem> {

    @Override
    public ButtonMenuItem bind(ButtonMenuItem compiled, BindProcessor p) {
        BadgeUtil.bindSimpleBadge(compiled.getBadge(), p);
        return compiled;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ButtonMenuItem.class;
    }
}
