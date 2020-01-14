package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Трансформатор доступа страницы
 */
@Component
public class PageAccessTransformer extends BaseAccessTransformer<Page, CompileContext<?, ?>> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Page.class;
    }

    @Override
    public Page transform(Page compiled, CompileContext context, CompileProcessor p) {
        SimpleCompiledAccessSchema accessSchema = (SimpleCompiledAccessSchema)
                p.getCompiled(new AccessContext(p.resolve(Placeholders.property("n2o.access.schema.id"), String.class)));
        collectPageAccess(compiled, context.getSourceId((BindProcessor) p), accessSchema, p);
        Map<String, Widget> widgets = compiled.getWidgets();
        if (compiled.getLayout() != null && compiled.getLayout().getRegions() != null) {
            for (List<Region> regions : compiled.getLayout().getRegions().values()) {
                for (Region region : regions) {
                    for (Region.Item item : region.getItems()) {
                        Widget w = widgets.get(item.getWidgetId());
                        transfer(w, item);
                    }
                }
            }
        }
        return compiled;
    }
}
