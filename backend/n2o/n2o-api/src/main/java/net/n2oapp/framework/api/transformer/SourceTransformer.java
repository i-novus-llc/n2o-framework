package net.n2oapp.framework.api.transformer;

import net.n2oapp.framework.api.metadata.ClientMetadata;
import net.n2oapp.framework.api.metadata.CompiledMetadata;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;

/**
 * Трансформация сурс модели
 */
@Deprecated
public interface SourceTransformer<T extends SourceMetadata, C extends CompileContext>
        extends Transformer<T, CompiledMetadata, ClientMetadata, C>, net.n2oapp.framework.api.metadata.compile.SourceTransformer<T>,
        SourceClassAware {


    @Override
    default T transform(T source) {
        return transformBeforeCompile(source, null);
    }

    @Override
    default T transformBeforeCompile(T metadata, C context) {
        return metadata;
    }

    @Override
    Class<T> getMetadataClass();

    @Override
    Class<C> getContextClass();

    @Override
    default Class<CompiledMetadata> getCompiledMetadataClass() {
        return null;
    }

    @Override
    default Class<ClientMetadata> getClientMetadataClass() {
        return null;
    }

    @Override
    default Class<? extends Source> getSourceClass() {
        return getMetadataClass();
    }
}
