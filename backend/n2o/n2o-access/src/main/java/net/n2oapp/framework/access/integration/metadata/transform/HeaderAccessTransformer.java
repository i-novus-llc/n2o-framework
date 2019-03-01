package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.header.CompiledHeader;
import net.n2oapp.framework.api.metadata.header.HeaderItem;
import net.n2oapp.framework.config.metadata.compile.context.HeaderContext;
import org.springframework.stereotype.Component;

/**
 * Трансформатор доступа хедера
 */
@Component
public class HeaderAccessTransformer extends BaseAccessTransformer<CompiledHeader, HeaderContext> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return CompiledHeader.class;
    }

    @Override
    public CompiledHeader transform(CompiledHeader compiled, HeaderContext context, CompileProcessor p) {
        SimpleCompiledAccessSchema accessSchema = (SimpleCompiledAccessSchema)
                p.getCompiled(new AccessContext(p.resolve(Placeholders.property("n2o.access.schema.id"), String.class)));
        mapSecurity(compiled, accessSchema, p);
        return compiled;
    }

    private void mapSecurity(CompiledHeader compiled, SimpleCompiledAccessSchema schema, CompileProcessor p) {
        for (HeaderItem item : compiled.getItems()) {
            if (item.getPageId() == null) continue;
            collectPageAccess(item, item.getPageId(), schema);
            String objectId = p.getSource(item.getPageId(), N2oPage.class).getObjectId();
            collectObjectAccess(item, objectId, null, schema);
        }
        for (HeaderItem item : compiled.getExtraItems()) {
            if (item.getPageId() == null) continue;
            collectPageAccess(item, item.getPageId(), schema);
            String objectId = p.getSource(item.getPageId(), N2oPage.class).getObjectId();
            collectObjectAccess(item, objectId, null, schema);
        }
    }
}
