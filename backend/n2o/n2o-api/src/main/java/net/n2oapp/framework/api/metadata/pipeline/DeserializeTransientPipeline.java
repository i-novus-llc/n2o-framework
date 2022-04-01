package net.n2oapp.framework.api.metadata.pipeline;

/**
 * Переходной конвейер десериализации json в исходные модели метаданных
 */
public interface DeserializeTransientPipeline<N extends Pipeline> extends Pipeline {

    N deserialize();

}
