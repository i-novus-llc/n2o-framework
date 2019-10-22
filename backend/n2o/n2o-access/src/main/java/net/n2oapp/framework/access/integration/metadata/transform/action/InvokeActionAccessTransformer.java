package net.n2oapp.framework.access.integration.metadata.transform.action;

import net.n2oapp.framework.access.integration.metadata.transform.BaseAccessTransformer;
import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import org.springframework.stereotype.Component;

/**
 * Трансформатор доступа invoke
 */
@Component
public class InvokeActionAccessTransformer extends BaseAccessTransformer<InvokeAction, CompileContext<?, ?>> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return InvokeAction.class;
    }

    @Override
    public InvokeAction transform(InvokeAction compiled, CompileContext context, CompileProcessor p) {
        SimpleCompiledAccessSchema accessSchema = (SimpleCompiledAccessSchema)
                p.getCompiled(new AccessContext(p.resolve(Placeholders.property("n2o.access.schema.id"), String.class)));
        mapSecurity(accessSchema, compiled, p);
        return compiled;
    }

    private void mapSecurity(SimpleCompiledAccessSchema schema, InvokeAction compiled, CompileProcessor p) {
        collectObjectAccess(compiled, compiled.getObjectId(), compiled.getOperationId(), schema, p);
    }
}
