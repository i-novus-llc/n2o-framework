package net.n2oapp.framework.config.metadata.transformer.factory;

import net.n2oapp.framework.api.metadata.ClientMetadata;
import net.n2oapp.framework.api.metadata.CompiledMetadata;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.transformer.SourceTransformer;
import net.n2oapp.framework.api.transformer.Transformer;

import java.util.List;

@Deprecated
public class SourceTransformerWrapper<T extends SourceMetadata, C extends CompileContext>
        extends TransformerWrapper<T, CompiledMetadata, ClientMetadata, C>
        implements SourceTransformer<T, C> {

    public SourceTransformerWrapper(Class<T> sourceMetadataClass,
                              Class<C> contextClass,
                              List<Transformer> transformers) {
        super(sourceMetadataClass, null, null, contextClass, transformers);
    }

}
