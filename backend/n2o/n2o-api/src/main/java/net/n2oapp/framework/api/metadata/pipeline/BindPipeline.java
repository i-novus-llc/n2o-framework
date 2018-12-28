package net.n2oapp.framework.api.metadata.pipeline;

public interface BindPipeline extends
        CompileProcessingPipeline<BindPipeline>,
        BindTransientPipeline<
                BindTerminalPipeline> {
}
