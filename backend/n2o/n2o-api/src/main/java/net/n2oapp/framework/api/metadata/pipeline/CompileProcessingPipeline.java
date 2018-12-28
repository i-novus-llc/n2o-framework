package net.n2oapp.framework.api.metadata.pipeline;

public interface CompileProcessingPipeline<T extends Pipeline> extends Pipeline {

    T transform();

    T cache();

    T copy();
}
