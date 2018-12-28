package net.n2oapp.framework.access.integration.metadata.transform.action;

import net.n2oapp.framework.access.integration.metadata.transform.BaseAccessTransformer;
import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.meta.action.show_modal.ShowModal;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

/**
 * Трансформатор доступа show-modal
 */
@Component
public class ShowModalAccessTransformer extends BaseAccessTransformer<ShowModal, PageContext> {


    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ShowModal.class;
    }

    @Override
    public ShowModal transform(ShowModal compiled, PageContext context, CompileProcessor p) {
        SimpleCompiledAccessSchema accessSchema = (SimpleCompiledAccessSchema)
                p.getCompiled(new AccessContext(p.resolve(Placeholders.property("n2o.access.schema.id"), String.class)));
        mapSecurity(accessSchema, compiled);
        return compiled;
    }

    private void mapSecurity(SimpleCompiledAccessSchema schema, ShowModal compiled) {
        collectObjectAccess(compiled, compiled.getObjectId(), compiled.getOperationId(), schema);
        collectPageAccess(compiled, compiled.getPageId(), schema);
    }
}
