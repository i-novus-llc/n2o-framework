package net.n2oapp.framework.api.metadata.pipeline;

/**
 * Переходной конвеер сохранения метаданных
 */
public interface PersistTransientPipeline<N extends Pipeline> extends Pipeline {

    N persist();

}