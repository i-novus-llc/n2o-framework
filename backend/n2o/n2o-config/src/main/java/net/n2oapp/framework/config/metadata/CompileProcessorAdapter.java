package net.n2oapp.framework.config.metadata;

import net.n2oapp.framework.api.metadata.ClientMetadata;
import net.n2oapp.framework.api.metadata.CompiledMetadata;
import net.n2oapp.framework.api.metadata.MetaModel;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.factory.EventCompilerFactory;
import net.n2oapp.framework.api.metadata.local.N2oCompiler;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.api.user.UserContext;

@Deprecated //use PipelineSupport or CompileProcessor
public class CompileProcessorAdapter implements N2oCompiler {
    private CompileProcessor adapter;
    private MetadataRegister metadataRegister;

    public CompileProcessorAdapter(CompileProcessor adapter, MetadataRegister metadataRegister) {
        this.adapter = adapter;
        this.metadataRegister = metadataRegister;
    }

    @Override
    public <T extends SourceMetadata> boolean isExists(String id, Class<T> metadataClass) {
        return metadataRegister.contains(id, metadataClass);
    }

    @Override
    public <T extends CompiledMetadata> T get(String id, Class<T> compiledClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClientMetadata map(String id, Class<? extends CompiledMetadata> compiledClass, UserContext user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <C extends CompiledMetadata, T extends SourceMetadata, D extends CompileContext> C compile(T n2o, D context) {
        return adapter.compile(n2o, context);
    }

    @Override
    public <D extends CompileContext> String register(D context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends CompiledMetadata, D extends CompileContext> T registerAndCompile(D context) {
        SourceMetadata source = adapter.getSource(context.getMetadataId(), context.getMetadataClass());
        return compile(source, context);
    }

    @Override
    public EventCompilerFactory getEventCompilerFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getMessage(String code, Object... arguments) {
        return adapter.getMessage(code, arguments);
    }

    @Override
    public MetaModel getMetaModelBySource(Class<? extends SourceMetadata> sourceMetadataClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MetaModel getMetaModelByCompiled(Class<? extends CompiledMetadata> compiledMetadataClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MetaModel getMetaModelByClient(Class<? extends ClientMetadata> clientMetadataClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends SourceMetadata> S getGlobal(String id, Class<S> n2oClass) {
        return adapter.getSource(id, n2oClass);
    }

    @Override
    public <T extends SourceMetadata, D extends CompileContext> D getContext(String id, Class<T> n2oClass) {
        throw new UnsupportedOperationException();
    }
}
