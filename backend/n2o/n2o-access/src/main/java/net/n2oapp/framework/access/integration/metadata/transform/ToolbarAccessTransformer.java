package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Button;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
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
    public Toolbar transform(Toolbar compiled, CompileContext context, CompileProcessor p) {
        for (List<Group> groupList : compiled.values()) {
            for (Group group : groupList) {
                for (Button b : group.getButtons()) {
                    if (b.getActionId() != null) {
                        Action action = p.getScope(MetaActions.class).get(b.getActionId());
                        transfer(action, b);
                    }
                }
            }
        }
        return compiled;
    }
}
