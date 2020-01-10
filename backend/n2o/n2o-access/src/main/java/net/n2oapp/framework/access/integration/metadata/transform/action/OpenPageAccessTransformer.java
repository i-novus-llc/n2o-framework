package net.n2oapp.framework.access.integration.metadata.transform.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkAction;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

/**
 * Трансформатор доступа open-page
 */
@Component
public class OpenPageAccessTransformer extends AbstractActionTransformer<LinkAction> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return LinkAction.class;
    }

    @Override
    public LinkAction transform(LinkAction compiled, PageContext context, CompileProcessor p) {
        mapSecurity(compiled, compiled.getPageId(), compiled.getObjectId(), compiled.getOperationId(), p);
        return compiled;
    }
}
