package net.n2oapp.framework.api.metadata.pipeline;

/**
 * Конвейер основной сборки метаданных
 */
public interface CompileTransientPipeline<N extends Pipeline> extends Pipeline {

    N compile();

}
