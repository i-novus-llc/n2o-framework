package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.menu.DropdownMenuItem;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

@Component
public class DropdownMenuItemBinder implements BaseMetadataBinder<DropdownMenuItem> {

    @Override
    public DropdownMenuItem bind(DropdownMenuItem compiled, BindProcessor p) {
        if (compiled.getContent() != null)
            compiled.getContent().forEach(p::bind);
        return compiled;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return DropdownMenuItem.class;
    }
}
