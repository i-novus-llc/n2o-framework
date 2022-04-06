package net.n2oapp.framework.api.metadata.pipeline;

/**
 * Конвейер чтения метаданных
 */
public interface ReadTransientPipeline<N extends Pipeline> extends Pipeline {

   N read();

}
