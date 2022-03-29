package net.n2oapp.framework.api.metadata.pipeline;

public interface DeserializeTransientPipeline<N extends Pipeline> extends Pipeline {

    N deserialize();

}
