package net.n2oapp.framework.api.metadata.pipeline;

/**
 * Конвеер чтения метаданных
 */
public interface ReadTransientPipeline<N extends Pipeline> extends Pipeline {

   N read();

}
