package net.n2oapp.framework.access.integration.metadata.transform.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

/**
 * Трансформатор доступа open-page
 */
@Component
public class OpenPageAccessTransformer extends AbstractActionTransformer<LinkActionImpl> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return LinkActionImpl.class;
    }

    @Override
    public LinkActionImpl transform(LinkActionImpl compiled, CompileContext<?, ?> context, CompileProcessor p) {
        mapSecurity(compiled, compiled.getPageId(), compiled.getObjectId(), compiled.getOperationId(), compiled.getUrl(), p);
        return compiled;
    }
}
