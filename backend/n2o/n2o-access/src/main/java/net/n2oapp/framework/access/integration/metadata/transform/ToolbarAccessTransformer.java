package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
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
            for (AbstractButton b : flattenButtons(groupList)) {
                if (b.getAction() != null) {
                    transfer(b.getAction(), b);
                }
            }
        }
        return compiled;
    }
}
