package net.n2oapp.framework.api.metadata.pipeline;

public interface SourceProcessingPipeline<T extends Pipeline> extends Pipeline {

    T validate();

    T merge();

    T transform();

    T cache();

    T copy();
}
