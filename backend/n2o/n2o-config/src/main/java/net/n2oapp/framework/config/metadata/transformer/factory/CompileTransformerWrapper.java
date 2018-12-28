package net.n2oapp.framework.config.metadata.transformer.factory;

import net.n2oapp.framework.api.metadata.ClientMetadata;
import net.n2oapp.framework.api.metadata.CompiledMetadata;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.transformer.CompileTransformer;
import net.n2oapp.framework.api.transformer.Transformer;

import java.util.List;

@Deprecated
public class CompileTransformerWrapper<D extends CompiledMetadata, C extends CompileContext>
        extends TransformerWrapper<SourceMetadata, D, ClientMetadata, C>
        implements CompileTransformer<D,C>  {

    public CompileTransformerWrapper(Class<D> compiledMetadataClass, Class<C> contextClass, List<Transformer> transformers) {
        super(null, compiledMetadataClass, null, contextClass, transformers);
    }
}
