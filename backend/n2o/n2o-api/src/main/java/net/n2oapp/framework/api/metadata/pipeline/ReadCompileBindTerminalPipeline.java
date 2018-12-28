package net.n2oapp.framework.api.metadata.pipeline;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;

public interface ReadCompileBindTerminalPipeline extends Pipeline,
        BindProcessingPipeline<ReadCompileBindTerminalPipeline> {

    <D extends Compiled> D get(CompileContext<D, ?> context, DataSet data);
}
