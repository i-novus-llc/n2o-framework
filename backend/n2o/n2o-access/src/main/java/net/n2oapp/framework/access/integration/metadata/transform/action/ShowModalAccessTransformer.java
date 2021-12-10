package net.n2oapp.framework.access.integration.metadata.transform.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

/**
 * Трансформатор доступа show-modal
 */
@Component
public class ShowModalAccessTransformer extends AbstractActionTransformer<ShowModal> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ShowModal.class;
    }

    @Override
    public ShowModal transform(ShowModal compiled, CompileContext<?, ?> context, CompileProcessor p) {
        mapSecurity(compiled, compiled.getPageId(), compiled.getObjectId(), compiled.getOperationId(), null, p);
        return compiled;
    }
}
