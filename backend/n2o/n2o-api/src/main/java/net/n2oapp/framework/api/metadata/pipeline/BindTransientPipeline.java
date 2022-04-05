package net.n2oapp.framework.api.metadata.pipeline;

/**
 * Конвейер связывания метаданных
 */
public interface BindTransientPipeline<N extends Pipeline> extends Pipeline {

    N bind();

}
