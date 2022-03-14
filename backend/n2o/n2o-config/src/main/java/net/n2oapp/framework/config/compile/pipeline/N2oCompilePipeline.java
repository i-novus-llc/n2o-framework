package net.n2oapp.framework.config.compile.pipeline;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.pipeline.CompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.CompilePipeline;
import net.n2oapp.framework.api.metadata.pipeline.CompileTerminalPipeline;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;

import static net.n2oapp.framework.api.metadata.pipeline.PipelineOperationType.*;

public class N2oCompilePipeline extends N2oPipeline implements CompilePipeline {

    protected N2oCompilePipeline(MetadataEnvironment env) {
        super(env);
    }

    @Override
    public CompileTerminalPipeline<CompileBindTerminalPipeline> compile() {
        pullOp(COMPILE);
        return new CompileTerminalPipeline<CompileBindTerminalPipeline>() {

            @Override
            public <D extends Compiled, S> D get(S input, CompileContext<?, ?> ctx) {
                return execute(ctx, null, input);
            }

            @Override
            public <D extends Compiled, S> D get(S input, CompileContext<?, ?> ctx, Object... scopes) {
                return execute(ctx, null, input, scopes);
            }

            @Override
            public <D extends Compiled, S> D get(S input, CompileContext<?, ?> ctx, CompileProcessor p) {
                return execute(ctx, null, input, (N2oCompileProcessor) p);
            }

            @Override
            public CompileBindTerminalPipeline bind() {
                pullOp(BIND);
                return new CompileBindTerminalPipeline() {
                    @Override
                    public <D extends Compiled, S> D get(S input, CompileContext<?, ?> context, DataSet data) {
                        return execute(context, data, input);
                    }

                    @Override
                    public <D extends Compiled, S> D get(S input, CompileContext<?, ?> context, DataSet data,
                                                         SubModelsProcessor subModelsProcessor) {
                        return execute(context, data, input, subModelsProcessor);
                    }

                    @Override
                    public CompileBindTerminalPipeline bind() {
                        pullOp(BIND);
                        return this;
                    }
                };
            }

            @Override
            public CompileTerminalPipeline<CompileBindTerminalPipeline> transform() {
                pullOp(COMPILE_TRANSFORM);
                return this;
            }

            @Override
            public CompileTerminalPipeline<CompileBindTerminalPipeline> cache() {
                pullOp(COMPILE_CACHE);
                return this;
            }

            @Override
            public CompileTerminalPipeline<CompileBindTerminalPipeline> copy() {
                pullOp(COPY);
                return this;
            }
        };
    }

    @Override
    public CompilePipeline validate() {
        pullOp(VALIDATE);
        return this;
    }

    @Override
    public CompilePipeline merge() {
        pullOp(MERGE);
        return this;
    }

    @Override
    public CompilePipeline transform() {
        pullOp(SOURCE_TRANSFORM);
        return this;
    }

    @Override
    public CompilePipeline cache() {
        pullOp(SOURCE_CACHE);
        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public CompilePipeline copy() {
        pullOp(COPY);
        return this;
    }
}
