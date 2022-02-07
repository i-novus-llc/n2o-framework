package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import org.springframework.stereotype.Component;

/**
 * Трансформатор доступа виджета
 */
@Component
public class WidgetAccessTransformer extends BaseAccessTransformer<Widget<?>, CompileContext<?, ?>> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Widget.class;
    }

    @Override
    public Widget<?> transform(Widget<?> compiled, CompileContext<?, ?> context, CompileProcessor p) {
        SimpleCompiledAccessSchema accessSchema = (SimpleCompiledAccessSchema)
                p.getCompiled(new AccessContext(p.resolve(Placeholders.property("n2o.access.schema.id"), String.class)));
        collectObjectAccess(compiled, compiled.getObjectId(), null, accessSchema, p);
        return compiled;
    }
}
