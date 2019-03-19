package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.toolbar.ToolbarCell;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Button;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import org.springframework.stereotype.Component;

@Component
public class ToolbarCellAccessTransformer extends BaseAccessTransformer<ToolbarCell, CompileContext<?, ?>> {

    @Override
    public ToolbarCell transform(ToolbarCell compiled, CompileContext<?, ?> context, CompileProcessor p) {
        MetaActions actions = p.getScope(MetaActions.class);
        if (actions != null) {
            for (Button button : compiled.getButtons()) {
                if (button.getAction() != null && actions.containsKey(button.getAction().getId())) {
                    Action action = actions.get(button.getAction().getId());
                    transfer(action, button);
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
