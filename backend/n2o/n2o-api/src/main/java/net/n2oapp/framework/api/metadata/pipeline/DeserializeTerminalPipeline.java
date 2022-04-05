package net.n2oapp.framework.api.metadata.pipeline;

import net.n2oapp.framework.api.metadata.SourceMetadata;

import java.io.InputStream;

/**
 * Заверщающий конвейер десериализации json в исходные модели метаданных
 */
public interface DeserializeTerminalPipeline<N extends Pipeline> extends Pipeline,
        SourceProcessingPipeline<DeserializeTerminalPipeline<N>>,
        PersistTransientPipeline<DeserializePersistTerminalPipeline> {

    <S extends SourceMetadata> S get(InputStream json, Class<S> sourceClass);

}