package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeUtil;
import net.n2oapp.framework.api.metadata.meta.menu.MenuItem;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

@Component
public class MenuItemBinder implements BaseMetadataBinder<MenuItem> {

    @Override
    public MenuItem bind(MenuItem compiled, BindProcessor p) {
        BadgeUtil.bindSimpleBadge(compiled.getBadge(), p);
        return compiled;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return MenuItem.class;
    }
}
