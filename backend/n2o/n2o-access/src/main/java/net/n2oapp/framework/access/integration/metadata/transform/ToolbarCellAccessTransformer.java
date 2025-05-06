package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.cell.ToolbarCell;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.*;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import org.springframework.stereotype.Component;

@Component
public class ToolbarCellAccessTransformer extends BaseAccessTransformer<ToolbarCell, CompileContext<?, ?>> {

    @Override
    public ToolbarCell transform(ToolbarCell compiled, CompileContext<?, ?> context, CompileProcessor p) {
        MetaActions actions = p.getScope(MetaActions.class);
        if (actions != null && compiled.getToolbar() != null) {
            for (Group group : compiled.getToolbar()) {
                for (AbstractButton b : group.getButtons()) {
                    if (b.getAction() != null) {
                        transfer(b.getAction(), b);
                    } else if (b instanceof Submenu submenu && submenu.getContent() != null) {
                        for (PerformButton menuItem : submenu.getContent()) {
                            if (menuItem.getAction() != null) {
                                transfer(menuItem.getAction(), menuItem);
                            }
                        }
                    }
                }
            }
        }
        return compiled;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ToolbarCell.class;
    }
}
