package net.n2oapp.framework.access.metadata.schema;

import net.n2oapp.framework.config.metadata.compile.context.BaseCompileContext;

/**
 * Контекст схемы прав доступа
 */
public class AccessContext extends BaseCompileContext<CompiledAccessSchema, N2oAccessSchema> {
    public AccessContext(String sourceId) {
        super(sourceId, N2oAccessSchema.class, CompiledAccessSchema.class);
    }
}
