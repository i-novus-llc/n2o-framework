package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import org.springframework.stereotype.Component;

/**
 * Трансформатор доступа объекта
 */
@Component
public class ObjectAccessTransformer extends BaseAccessTransformer<CompiledObject, ObjectContext> {

    @Override
    public CompiledObject transform(CompiledObject compiled, ObjectContext context, CompileProcessor p) {
        SimpleCompiledAccessSchema accessSchema = (SimpleCompiledAccessSchema)
                p.getCompiled(new AccessContext(p.resolve(Placeholders.property("n2o.access.schema.id"), String.class)));
        mapSecurity(accessSchema, compiled);
        return compiled;
    }

    private void mapSecurity(SimpleCompiledAccessSchema schema, CompiledObject compiled) {
        for (CompiledObject.Operation operation : compiled.getOperations().values()) {
            mapObjectAccess(schema, operation, compiled.getId());
        }
    }

    private void mapObjectAccess(SimpleCompiledAccessSchema schema, CompiledObject.Operation compiled, String objectId) {
        collectObjectAccess(compiled, objectId, compiled.getId(), schema);
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return CompiledObject.class;
    }
}
