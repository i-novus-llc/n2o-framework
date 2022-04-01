package net.n2oapp.framework.api.metadata.pipeline;

/**
 * Переходной конвейер сериализации исходных метеданных в json
 */
public interface SerializeTransientPipeline<N extends Pipeline> extends Pipeline {

    N serialize();

}
