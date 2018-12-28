package net.n2oapp.framework.api.transformer;

import net.n2oapp.framework.api.metadata.ClientMetadata;
import net.n2oapp.framework.api.metadata.CompiledMetadata;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.user.UserContext;

/**
 * Трансформация метаданных под пользователя
 */
@Deprecated
public interface ClientTransformer<T extends ClientMetadata, C extends CompileContext> extends  Transformer<SourceMetadata, CompiledMetadata, T, C> {

    @Override
    default T transformAfterMap(T metadata, C context, UserContext user) {
        return metadata;
    }

    @Override
    default Class<CompiledMetadata> getCompiledMetadataClass() {
        return null;
    }

    @Override
    default Class<SourceMetadata> getMetadataClass() {
        return null;
    }

    @Override
    Class<T> getClientMetadataClass();

    @Override
    Class<C> getContextClass();
}
