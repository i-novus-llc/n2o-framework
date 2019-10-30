package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.toolbar.ToolbarCell;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Button;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.MenuItem;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ToolbarCellAccessTransformer extends BaseAccessTransformer<ToolbarCell, CompileContext<?, ?>> {

    @Override
    public ToolbarCell transform(ToolbarCell compiled, CompileContext<?, ?> context, CompileProcessor p) {
        MetaActions actions = p.getScope(MetaActions.class);
        if (actions != null && compiled.getToolbar() != null) {
            for (Group group : compiled.getToolbar()) {
                for (Button b : group.getButtons()) {
                    if (b.getActionId() != null) {
                        Action action = p.getScope(MetaActions.class).get(b.getActionId());
                        transfer(action, b);
                    } else if (b.getSubMenu() != null) {
                        for (MenuItem menuItem : b.getSubMenu()) {
                            if (menuItem.getActionId() != null) {
                                Action action = p.getScope(MetaActions.class).get(menuItem.getActionId());
                                transfer(action, menuItem);
                            }
                        }
                        merge(b, b.getSubMenu());
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
