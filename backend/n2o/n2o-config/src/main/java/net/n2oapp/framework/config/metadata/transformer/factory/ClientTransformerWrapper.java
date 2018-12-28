package net.n2oapp.framework.config.metadata.transformer.factory;

import net.n2oapp.framework.api.metadata.ClientMetadata;
import net.n2oapp.framework.api.metadata.CompiledMetadata;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.transformer.ClientTransformer;
import net.n2oapp.framework.api.transformer.Transformer;

import java.util.List;

@Deprecated
public class ClientTransformerWrapper<M extends ClientMetadata,
        C extends CompileContext>
        extends TransformerWrapper<SourceMetadata, CompiledMetadata, M, C>
        implements ClientTransformer<M, C> {
    public ClientTransformerWrapper(Class<M> clientMetadataClass, Class<C> contextClass, List<Transformer> transformers) {
        super(null, null, clientMetadataClass, contextClass, transformers);
    }
}
