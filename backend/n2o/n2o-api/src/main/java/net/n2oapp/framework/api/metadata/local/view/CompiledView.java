package net.n2oapp.framework.api.metadata.local.view;

import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.local.AbstractCompiledMetadata;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.metadata.CompiledMetadata;

/**
 * User: operhod
 * Date: 06.11.13
 * Time: 12:52
 */
public abstract class CompiledView<T extends N2oMetadata, D extends CompileContext>
        extends AbstractCompiledMetadata<T, D> {

    public abstract String getSrc();

}
