package net.n2oapp.framework.api.metadata.pipeline;

public interface CompilePipeline extends
        SourceProcessingPipeline<CompilePipeline>,
        CompileTransientPipeline<
                CompileTerminalPipeline<
                        CompileBindTerminalPipeline>> {
}
