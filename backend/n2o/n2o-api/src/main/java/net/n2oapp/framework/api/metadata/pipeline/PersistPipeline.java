package net.n2oapp.framework.api.metadata.pipeline;

/**
 * Общий конвейер сохранения метаданных
 */
public interface PersistPipeline extends
        SourceProcessingPipeline<PersistPipeline>,
        PersistTransientPipeline<PersistTerminalPipeline> {
}
