package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.Sidebar;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.header.Header;
import net.n2oapp.framework.api.metadata.header.HeaderItem;
import net.n2oapp.framework.api.metadata.header.SimpleMenu;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Трансформатор доступа к элементам приложения
 */
@Component
public class ApplicationAccessTransformer extends BaseAccessTransformer<Application, ApplicationContext> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Application.class;
    }

    @Override
    public Application transform(Application compiled, ApplicationContext context, CompileProcessor p) {
        SimpleCompiledAccessSchema accessSchema = (SimpleCompiledAccessSchema)
                p.getCompiled(new AccessContext(p.resolve(Placeholders.property("n2o.access.schema.id"), String.class)));
        transformHeader(compiled.getHeader(), accessSchema, p);
        transformSidebar(compiled.getSidebar(), accessSchema, p);
        return compiled;
    }

    private void transformHeader(Header compiled, SimpleCompiledAccessSchema schema, CompileProcessor p) {
        if (compiled == null) return;
        mapSecurityItems(compiled.getMenu(), schema, p);
        mapSecurityItems(compiled.getExtraMenu(), schema, p);
    }

    private void transformSidebar(Sidebar compiled, SimpleCompiledAccessSchema schema, CompileProcessor p) {
        if (compiled == null) return;
        mapSecurityItems(compiled.getMenu(), schema, p);
        mapSecurityItems(compiled.getExtraMenu(), schema, p);
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
            collectUrlAccess(si, si.getHref(), schema, p);
        } else {
            String objectId = p.getSource(si.getPageId(), N2oPage.class).getObjectId();
            collectObjectAccess(si, objectId, null, schema, p);
        }
    }
}
