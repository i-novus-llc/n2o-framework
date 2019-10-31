package net.n2oapp.framework.access.integration.metadata.transform.action;

import net.n2oapp.framework.access.integration.metadata.transform.BaseAccessTransformer;
import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkAction;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

/**
 * Трансформатор доступа open-page
 */
@Component
public class OpenPageAccessTransformer extends BaseAccessTransformer<LinkAction, PageContext> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return LinkAction.class;
    }

    @Override
    public LinkAction transform(LinkAction compiled, PageContext context, CompileProcessor p) {
        SimpleCompiledAccessSchema accessSchema = (SimpleCompiledAccessSchema)
                p.getCompiled(new AccessContext(p.resolve(Placeholders.property("n2o.access.schema.id"), String.class)));
        mapSecurity(accessSchema, compiled, p);
        return compiled;
    }

    private void mapSecurity(SimpleCompiledAccessSchema schema, LinkAction compiled, CompileProcessor p) {
        collectObjectAccess(compiled, compiled.getObjectId(), compiled.getOperationId(), schema, p);
        collectPageAccess(compiled, compiled.getPageId(), schema, p);
    }
}
