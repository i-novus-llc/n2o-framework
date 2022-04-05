package net.n2oapp.framework.api.metadata.pipeline;

/**
 * Общий конвейер десериализации json в исходные модели метаданных
 */
public interface DeserializePipeline extends
        DeserializeTransientPipeline<
            DeserializeTerminalPipeline<
                DeserializePersistTerminalPipeline>> {
}
