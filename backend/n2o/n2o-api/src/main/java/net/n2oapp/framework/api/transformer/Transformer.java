package net.n2oapp.framework.api.transformer;

import net.n2oapp.framework.api.metadata.ClientMetadata;
import net.n2oapp.framework.api.metadata.CompiledMetadata;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.user.UserContext;

/**
 * Трансформация метаданных
 */
@Deprecated
public interface Transformer<T extends SourceMetadata,
        D extends CompiledMetadata,
        M extends ClientMetadata,
        C extends CompileContext> {

    default T transformBeforeValidate(T metadata, C context) {
        return metadata;
    }

    default T transformBeforeCompile(T metadata, C context) {
        return metadata;
    }

    default D transformAfterCompile(D metadata, C context) {
        return metadata;
    }

    default D transformBeforeMap(D metadata, C context, UserContext user) {
        return metadata;
    }

    default M transformAfterMap(M metadata, C context, UserContext user) {
        return metadata;
    }

    Class<D> getCompiledMetadataClass();

    Class<T> getMetadataClass();

    Class<M> getClientMetadataClass();

    Class<C> getContextClass();
}
