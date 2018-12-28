package net.n2oapp.framework.api.metadata.pipeline;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;

public interface CompileBindTerminalPipeline extends Pipeline,
        BindProcessingPipeline<CompileBindTerminalPipeline> {

    <D extends Compiled, S> D get(S input, CompileContext<?,?> context, DataSet data);
}
