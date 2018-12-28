package net.n2oapp.framework.api.metadata.pipeline;

public interface BindProcessingPipeline<T extends Pipeline> extends Pipeline {
    T bind();
}
