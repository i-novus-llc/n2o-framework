package net.n2oapp.framework.api.metadata.pipeline;

public interface SerializeTransientPipeline<N extends Pipeline> extends Pipeline {

    N serialize();

}
