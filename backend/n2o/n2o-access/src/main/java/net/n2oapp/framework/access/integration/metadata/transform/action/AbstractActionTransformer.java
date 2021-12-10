package net.n2oapp.framework.access.integration.metadata.transform.action;

import net.n2oapp.framework.access.integration.metadata.transform.BaseAccessTransformer;
import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;

public abstract class AbstractActionTransformer<D extends Action> extends BaseAccessTransformer<D, CompileContext<?, ?>> {

    protected void mapSecurity(AbstractAction compiled, String pageId, String objectId, String operationId, String url, CompileProcessor p) {
        SimpleCompiledAccessSchema accessSchema = (SimpleCompiledAccessSchema)
                p.getCompiled(new AccessContext(p.resolve(Placeholders.property("n2o.access.schema.id"), String.class)));
        if (objectId == null && pageId != null && !StringUtils.hasLink(pageId))
            objectId = p.getSource(pageId, N2oPage.class).getObjectId();
        collectObjectAccess(compiled, objectId, operationId, accessSchema, p);
        collectPageAccess(compiled, pageId, accessSchema, p);
        collectUrlAccess(compiled, url, accessSchema, p);
    }

}
