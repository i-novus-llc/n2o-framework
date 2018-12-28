package net.n2oapp.framework.api.metadata.pipeline;

public interface ReadPipeline extends
        ReadTransientPipeline<
                ReadTerminalPipeline<
                        ReadCompileTerminalPipeline<
                                ReadCompileBindTerminalPipeline>>> {
}
