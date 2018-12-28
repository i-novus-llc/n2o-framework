package net.n2oapp.framework.api.metadata.pipeline;

import net.n2oapp.framework.api.metadata.SourceMetadata;

public interface ReadTerminalPipeline<N extends Pipeline> extends Pipeline,
        SourceProcessingPipeline<ReadTerminalPipeline<N>>,
        CompileTransientPipeline<N> {

    <S extends SourceMetadata> S get(String id, Class<S> sourceClass);

}
