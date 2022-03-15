package net.n2oapp.framework.api.metadata.pipeline;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;

public interface CompileTerminalPipeline<N extends Pipeline> extends Pipeline,
        CompileProcessingPipeline<CompileTerminalPipeline<N>>,
        BindTransientPipeline<N> {

    <D extends Compiled, S> D get(S input, CompileContext<?, ?> ctx);

    <D extends Compiled, S> D get(S input, CompileContext<?, ?> ctx, Object... scopes);

    <D extends Compiled, S> D get(S input, CompileContext<?, ?> ctx, CompileProcessor p);
}
