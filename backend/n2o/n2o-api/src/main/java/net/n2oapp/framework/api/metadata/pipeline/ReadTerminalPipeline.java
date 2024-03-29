package net.n2oapp.framework.api.metadata.pipeline;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;

/**
 * Завершающий конвейер чтения метаданных
 */
public interface ReadTerminalPipeline<N extends Pipeline> extends Pipeline,
        SourceProcessingPipeline<ReadTerminalPipeline<N>>,
        CompileTransientPipeline<N>,
        PersistTransientPipeline<ReadPersistTerminalPipeline>,
        SerializeTransientPipeline<ReadSerializeTerminalPipeline> {

    <S extends SourceMetadata> S get(String id, Class<S> sourceClass);

    <S extends SourceMetadata> S get(String id, Class<S> sourceClass, SourceProcessor p);

}
