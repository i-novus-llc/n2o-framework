package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.menu.GroupMenuItem;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

@Component
public class GroupMenuItemBinder implements BaseMetadataBinder<GroupMenuItem> {

    @Override
    public GroupMenuItem bind(GroupMenuItem compiled, BindProcessor p) {
        if (compiled.getContent() != null)
            compiled.getContent().forEach(p::bind);
        return compiled;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return GroupMenuItem.class;
    }
}
