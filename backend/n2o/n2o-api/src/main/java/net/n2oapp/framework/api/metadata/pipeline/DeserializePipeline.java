package net.n2oapp.framework.api.metadata.pipeline;

public interface DeserializePipeline extends
        DeserializeTransientPipeline<
            DeserializeTerminalPipeline<
                DeserializePersistTerminalPipeline>> {
}
