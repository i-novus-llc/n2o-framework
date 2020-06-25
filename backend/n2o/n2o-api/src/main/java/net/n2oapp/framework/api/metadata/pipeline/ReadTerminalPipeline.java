package net.n2oapp.framework.api.metadata.pipeline;

import net.n2oapp.framework.api.metadata.SourceMetadata;

/**
 * Завершающий конвеер чтения метаданных
 */
public interface ReadTerminalPipeline<N extends Pipeline> extends Pipeline,
        SourceProcessingPipeline<ReadTerminalPipeline<N>>,
        CompileTransientPipeline<N>,
        PersistTransientPipeline<ReadPersistTerminalPipeline> {

    <S extends SourceMetadata> S get(String id, Class<S> sourceClass);

}
