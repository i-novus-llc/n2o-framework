package net.n2oapp.framework.config.metadata.transformer.factory;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.N2oMetadataException;
import net.n2oapp.framework.api.metadata.ClientMetadata;
import net.n2oapp.framework.api.metadata.CompiledMetadata;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.transformer.Transformer;
import net.n2oapp.framework.api.user.UserContext;

import java.util.List;
import java.util.function.BiFunction;

@Deprecated
public class TransformerWrapper<T extends SourceMetadata,
        D extends CompiledMetadata,
        M extends ClientMetadata,
        C extends CompileContext>
        implements Transformer<T, D, M, C> {
    private Class<T> sourceMetadataClass;
    private Class<D> compiledMetadataClass;
    private Class<M> clientMetadataClass;
    private Class<C> contextClass;
    private List<Transformer> transformers;

    public TransformerWrapper(Class<T> sourceMetadataClass,
                              Class<D> compiledMetadataClass,
                              Class<M> clientMetadataClass,
                              Class<C> contextClass,
                              List<Transformer> transformers) {
        this.sourceMetadataClass = sourceMetadataClass;
        this.compiledMetadataClass = compiledMetadataClass;
        this.clientMetadataClass = clientMetadataClass;
        this.contextClass = contextClass;
        this.transformers = transformers;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T transformBeforeValidate(T metadata, C context) {
        return innerTransform(metadata, (t, m) -> (T) t.transformBeforeValidate(m, context));
    }

    @SuppressWarnings("unchecked")
    @Override
    public T transformBeforeCompile(T metadata, C context) {
        return innerTransform(metadata, (t, m) -> (T) t.transformBeforeCompile(m, context));
    }

    @SuppressWarnings("unchecked")
    @Override
    public D transformAfterCompile(D metadata, C context) {
        return innerTransform(metadata, (t, m) -> (D) t.transformAfterCompile(m, context));
    }

    @SuppressWarnings("unchecked")
    @Override
    public D transformBeforeMap(D metadata, C context, UserContext user) {
        return innerTransform(metadata, (t, m) -> (D) t.transformBeforeMap(m, context, user));
    }

    @SuppressWarnings("unchecked")
    @Override
    public M transformAfterMap(M metadata, C context, UserContext user) {
        return innerTransform(metadata, (t, m) -> (M) t.transformAfterMap(m, context, user));
    }

    @Override
    public Class<D> getCompiledMetadataClass() {
        return compiledMetadataClass;
    }

    @Override
    public Class<T> getMetadataClass() {
        return sourceMetadataClass;
    }

    @Override
    public Class<M> getClientMetadataClass() {
        return clientMetadataClass;
    }

    @Override
    public Class<C> getContextClass() {
        return contextClass;
    }

    protected <X> X innerTransform(X metadata, BiFunction<Transformer, X, X> callback) {
        X m = metadata;
        for (Transformer transformer : transformers) {
            try {
                m = callback.apply(transformer, m);
            } catch (N2oException e) {
                if (m instanceof SourceMetadata)
                    throw new N2oMetadataException((SourceMetadata) m, e.getMessage(), e);
                if (m instanceof CompiledMetadata)
                    throw new N2oMetadataException((CompiledMetadata) m, e.getMessage(), e);
                if (m instanceof ClientMetadata)
                    throw new N2oMetadataException((ClientMetadata) m, e.getMessage(), e);
            }
        }
        return m;
    }
}
