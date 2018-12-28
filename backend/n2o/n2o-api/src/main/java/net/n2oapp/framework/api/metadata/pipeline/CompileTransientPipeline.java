package net.n2oapp.framework.api.metadata.pipeline;

/**
 * Конвеер основной сборки метаданных
 */
public interface CompileTransientPipeline<N extends Pipeline> extends Pipeline {

    N compile();

}
