package net.n2oapp.framework.access.metadata.compile;

import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.CompiledAccessSchema;
import net.n2oapp.framework.access.metadata.schema.N2oAccessSchema;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;

/**
 * Абстрактная компиляции схемы прав доступа
 */
public abstract class AccessSchemaCompiler<D extends CompiledAccessSchema, S extends N2oAccessSchema> implements BaseSourceCompiler<D, S, AccessContext> {

    protected void compileAccess(D compiled, S source, AccessContext context, CompileProcessor p) {
        compiled.setId(context.getCompiledId(p));
    }
}
