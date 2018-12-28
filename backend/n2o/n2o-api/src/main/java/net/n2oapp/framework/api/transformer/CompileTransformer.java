package net.n2oapp.framework.api.transformer;

import net.n2oapp.framework.api.metadata.ClientMetadata;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.CompiledMetadata;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.CompiledClassAware;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.user.UserContext;

/**
 * Транфсормация скомпилированных метаданных
 */
@Deprecated
public interface CompileTransformer<T extends CompiledMetadata, C extends CompileContext>
        extends Transformer<SourceMetadata, T, ClientMetadata, C>,
        net.n2oapp.framework.api.metadata.compile.CompileTransformer<T, net.n2oapp.framework.api.metadata.compile.CompileContext<?,?>>,
        CompiledClassAware {

    @Override
    default T transform(T compiled, net.n2oapp.framework.api.metadata.compile.CompileContext<?, ?> context, CompileProcessor p) {
        return transformAfterCompile(compiled, null);
    }

    @Override
    default T transformAfterCompile(T metadata, C context) {
        return metadata;
    }

    @Override
    default T transformBeforeMap(T metadata, C context, UserContext user) {
        return metadata;
    }

    @Override
    Class<T> getCompiledMetadataClass();

    @Override
    Class<C> getContextClass();

    @Override
    default Class<SourceMetadata> getMetadataClass() {
        return null;
    }

    @Override
    default Class<ClientMetadata> getClientMetadataClass() {
        return null;
    }

    @Override
    default Class<? extends Compiled> getCompiledClass() {
        return getCompiledMetadataClass();
    }
}
