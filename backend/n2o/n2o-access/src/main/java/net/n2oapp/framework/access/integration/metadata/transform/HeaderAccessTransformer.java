package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.header.CompiledHeader;
import net.n2oapp.framework.api.metadata.header.HeaderItem;
import net.n2oapp.framework.api.metadata.header.SimpleMenu;
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
        mapSecurityItems(compiled.getItems(), schema, p);
        mapSecurityItems(compiled.getExtraItems(), schema, p);
    }

    private void mapSecurityItems(SimpleMenu items, SimpleCompiledAccessSchema schema, CompileProcessor p) {
        for (HeaderItem item : items) {
            if (item.getSubItems() == null) {
                mapSecurityItem(schema, p, item);
            } else {
                item.getSubItems().forEach(si -> mapSecurityItem(schema, p, si));
            }
        }
    }

    private void mapSecurityItem(SimpleCompiledAccessSchema schema, CompileProcessor p, HeaderItem si) {
        collectPageAccess(si, si.getPageId(), schema, p);
        if (si.getPageId() == null) {
            //todo collectUrlAccess
        } else {
            String objectId = p.getSource(si.getPageId(), N2oPage.class).getObjectId();
            collectObjectAccess(si, objectId, null, schema, p);
        }
    }
}
