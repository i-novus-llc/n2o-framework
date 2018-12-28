package net.n2oapp.framework.config.metadata.compile.context;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.local.CompiledObject;

/**
 * Контекст сборки объекта
 */
public class ObjectContext extends BaseCompileContext<CompiledObject, N2oObject> {

    public ObjectContext(String objectId) {
        super(objectId, N2oObject.class, CompiledObject.class);
    }

    public ObjectContext(String objectId, String route) {
        super(route, objectId, N2oObject.class, CompiledObject.class);
    }
}
