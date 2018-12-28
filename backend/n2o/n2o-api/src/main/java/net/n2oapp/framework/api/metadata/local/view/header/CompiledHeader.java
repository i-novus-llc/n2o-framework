package net.n2oapp.framework.api.metadata.local.view.header;

import net.n2oapp.framework.api.metadata.ClientMetadata;
import net.n2oapp.framework.api.metadata.CompiledMetadata;
import net.n2oapp.framework.api.metadata.header.N2oHeader;
import net.n2oapp.framework.api.metadata.local.N2oCompiler;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.metadata.local.view.CompiledView;

import java.util.Set;

/**
 * User: operhod
 * Date: 13.01.14
 * Time: 15:45
 */
@Deprecated
public abstract class CompiledHeader<N extends N2oHeader> extends CompiledView<N, CompileContext> implements ClientMetadata {

    @Override
    public void compile(N n2oHeader, final N2oCompiler compiler, CompileContext context) {
        super.compile(n2oHeader, compiler, context);
    }

    @Override
    public final Class<? extends CompiledMetadata> getCompiledBaseClass() {
        return CompiledHeader.class;
    }

    @Override
    public abstract String getSrc();

    public abstract void removePage(String pageId);

    public abstract Set<String> getAllPageIds();

}



