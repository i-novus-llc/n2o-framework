package net.n2oapp.framework.api.metadata.pipeline;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;

public interface ReadCompileTerminalPipeline<N extends Pipeline> extends Pipeline,
        CompileProcessingPipeline<ReadCompileTerminalPipeline<N>>,
        BindTransientPipeline<N> {

    default <D extends Compiled> D get(CompileContext<D,?> ctx) {
        return get(ctx, null);
    }

    <D extends Compiled> D get(CompileContext<D,?> ctx, CompileProcessor p);
}
