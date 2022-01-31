package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Трансформатор доступа тулбара
 */
@Component
public class ToolbarAccessTransformer extends BaseAccessTransformer<Toolbar, CompileContext<?, ?>> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Toolbar.class;
    }

    @Override
    public Toolbar transform(Toolbar compiled, CompileContext<?, ?> context, CompileProcessor p) {
        for (List<Group> groupList : compiled.values()) {
            for (Group group : groupList) {
                for (AbstractButton b : group.getButtons()) {
                    if (b.getAction() != null) {
                        transfer(b.getAction(), b);
                    } else if (b instanceof Submenu && ((Submenu) b).getSubMenu() != null) {
                        for (PerformButton menuItem : ((Submenu) b).getSubMenu()) {
                            if (menuItem.getAction() != null) {
                                transfer(menuItem.getAction(), menuItem);
                            }
                        }
                        merge(b, ((Submenu) b).getSubMenu());
                    }
                }
            }
        }
        return compiled;
    }
}
