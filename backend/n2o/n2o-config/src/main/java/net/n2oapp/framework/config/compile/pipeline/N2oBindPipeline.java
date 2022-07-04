package net.n2oapp.framework.config.compile.pipeline;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.pipeline.BindPipeline;
import net.n2oapp.framework.api.metadata.pipeline.BindTerminalPipeline;
import net.n2oapp.framework.api.util.SubModelsProcessor;

import static net.n2oapp.framework.api.metadata.pipeline.PipelineOperationType.*;

public class N2oBindPipeline extends N2oPipeline implements BindPipeline {

    protected N2oBindPipeline(MetadataEnvironment env) {
        super(env);
    }

    @Override
    public BindTerminalPipeline bind() {
        pullOp(BIND);
        return new BindTerminalPipeline() {
            @Override
            public <D extends Compiled> D get(D input, CompileContext<?, ?> context, DataSet data) {
                return execute(context, data, input);
            }

            @Override
            public <D extends Compiled> D get(D input, CompileContext<?, ?> context, DataSet data,
                                              SubModelsProcessor subModelsProcessor, Object... scopes) {
                return execute(context, data, input, subModelsProcessor, scopes);
            }

            @Override
            public BindTerminalPipeline bind() {
                pullOp(BIND);
                return this;
            }
        };
    }

    @Override
    public BindPipeline transform() {
        pullOp(COMPILE_TRANSFORM);
        return this;
    }

    @Override
    public BindPipeline cache() {
        pullOp(COMPILE_CACHE);
        return this;
    }

    @Override
    public BindPipeline copy() {
        pullOp(COPY);
        return this;
    }
}
