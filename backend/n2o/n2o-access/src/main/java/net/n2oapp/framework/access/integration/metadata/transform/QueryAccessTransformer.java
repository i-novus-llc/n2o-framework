package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import org.springframework.stereotype.Component;

/**
 * Трансформатор доступа выборки
 */
@Component
public class QueryAccessTransformer extends BaseAccessTransformer<CompiledQuery, QueryContext> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return CompiledQuery.class;
    }

    @Override
    public CompiledQuery transform(CompiledQuery compiled, QueryContext context, CompileProcessor p) {
        SimpleCompiledAccessSchema accessSchema = (SimpleCompiledAccessSchema) p.getCompiled(
                new AccessContext(p.resolve(Placeholders.property("n2o.access.schema.id"), String.class)));
        mapSecurity(compiled, accessSchema);
        return compiled;
    }

    private void mapSecurity(CompiledQuery compiled, SimpleCompiledAccessSchema schema) {
        if (compiled.getObject() != null) {
            collectObjectAccess(compiled, compiled.getObject().getId(), "read", schema);
            collectObjectFilters(compiled, compiled.getObject().getId(), null, schema);
        }
    }
}
