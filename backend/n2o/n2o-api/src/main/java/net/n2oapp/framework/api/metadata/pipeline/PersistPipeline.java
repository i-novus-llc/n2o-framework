package net.n2oapp.framework.api.metadata.pipeline;

/**
 * Общий конвеер сохранения метаданных
 */
public interface PersistPipeline extends
        SourceProcessingPipeline<PersistPipeline>,
        PersistTransientPipeline<PersistTerminalPipeline> {
}
