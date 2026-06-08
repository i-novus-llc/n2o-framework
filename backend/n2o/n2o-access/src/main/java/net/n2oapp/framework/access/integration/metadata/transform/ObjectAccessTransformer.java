package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Трансформатор доступа объекта
 */
@Component
public class ObjectAccessTransformer extends BaseAccessTransformer<CompiledObject, ObjectContext> {

    @Override
    public CompiledObject transform(CompiledObject compiled, ObjectContext context, CompileProcessor p) {
        SimpleCompiledAccessSchema accessSchema = (SimpleCompiledAccessSchema)
                p.getCompiled(new AccessContext(p.resolve(Placeholders.property("n2o.access.schema.id"), String.class)));
        mapSecurity(accessSchema, compiled, p);
        return compiled;
    }

    private void mapSecurity(SimpleCompiledAccessSchema schema, CompiledObject compiled, CompileProcessor p) {
        for (CompiledObject.Operation operation : compiled.getOperations().values()) {
            mapObjectAccess(schema, operation, compiled.getId(), p);
            compileFieldSecurity(operation, p);
        }
    }

    private void mapObjectAccess(SimpleCompiledAccessSchema schema, CompiledObject.Operation compiled, String objectId, CompileProcessor p) {
        collectObjectAccess(compiled, objectId, compiled.getId(), schema, p);
        collectObjectFilters(compiled, objectId, compiled.getId(), schema);
    }

    private void compileFieldSecurity(CompiledObject.Operation operation, CompileProcessor p) {
        putFieldSecurity(operation, operation.getInParametersMap(), Security.IN_FIELD_SECURITY_PROP_NAME, p);
        putFieldSecurity(operation, operation.getOutParametersMap(), Security.OUT_FIELD_SECURITY_PROP_NAME, p);
    }

    private void putFieldSecurity(CompiledObject.Operation operation, Map<String, AbstractParameter> params,
                                  String propName, CompileProcessor p) {
        if (params == null) return;
        Map<String, Security> fieldSecurity = new LinkedHashMap<>();
        collectFieldSecurity(params.values().toArray(new AbstractParameter[0]), "", fieldSecurity, p);
        if (!fieldSecurity.isEmpty()) {
            if (operation.getProperties() == null) operation.setProperties(new HashMap<>());
            operation.getProperties().put(propName, fieldSecurity);
        }
    }

    private void collectFieldSecurity(AbstractParameter[] params, String prefix,
                                      Map<String, Security> fieldSecurity, CompileProcessor p) {
        if (params == null) return;
        for (AbstractParameter param : params) {
            String key = prefix.isEmpty() ? param.getId() : prefix + "." + param.getId();
            Map<String, Object> attrs = p.mapAttributes(param);
            if (attrs != null && attrs.containsKey(Security.SECURITY_PROP_NAME))
                fieldSecurity.put(key, (Security) attrs.get(Security.SECURITY_PROP_NAME));
            if (param instanceof ObjectReferenceField ref && ref.getFields() != null)
                collectFieldSecurity(ref.getFields(), key, fieldSecurity, p);
        }
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return CompiledObject.class;
    }
}
