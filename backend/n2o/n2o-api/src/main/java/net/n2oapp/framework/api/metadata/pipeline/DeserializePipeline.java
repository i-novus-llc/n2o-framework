package net.n2oapp.framework.api.metadata.pipeline;

/**
 * Общий конвейер десериализации json в исходные модели метеданных
 */
public interface DeserializePipeline extends
        DeserializeTransientPipeline<
            DeserializeTerminalPipeline<
                DeserializePersistTerminalPipeline>> {
}
