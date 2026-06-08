package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.global.dao.query.AbstractField;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QueryReferenceField;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        mapSecurity(compiled, accessSchema, p);
        compileFieldSecurity(compiled, p);
        return compiled;
    }

    private void compileFieldSecurity(CompiledQuery compiled, CompileProcessor p) {
        if (compiled.getDisplayFields() == null) return;
        Map<String, Object> fieldSecurity = new LinkedHashMap<>();
        collectFieldSecurity(compiled.getDisplayFields(), fieldSecurity, p);
        if (!fieldSecurity.isEmpty()) {
            if (compiled.getProperties() == null) compiled.setProperties(new HashMap<>());
            compiled.getProperties().put(Security.FIELD_SECURITY_PROP_NAME, fieldSecurity);
        }
    }

    private void collectFieldSecurity(List<AbstractField> fields, Map<String, Object> fieldSecurity, CompileProcessor p) {
        for (AbstractField field : fields) {
            Map<String, Object> attrs = p.mapAttributes(field);
            if (attrs != null && attrs.containsKey(Security.SECURITY_PROP_NAME))
                fieldSecurity.put(field.getAbsoluteId(), attrs.get(Security.SECURITY_PROP_NAME));
            if (field instanceof QueryReferenceField ref && ref.getFields() != null)
                collectFieldSecurity(Arrays.asList(ref.getFields()), fieldSecurity, p);
        }
    }

    private void mapSecurity(CompiledQuery compiled, SimpleCompiledAccessSchema schema, CompileProcessor p) {
        if (compiled.getObject() != null) {
            collectObjectAccess(compiled, compiled.getObject().getId(), null, schema, p);
            collectObjectFilters(compiled, compiled.getObject().getId(), null, schema);
        }
    }
}
