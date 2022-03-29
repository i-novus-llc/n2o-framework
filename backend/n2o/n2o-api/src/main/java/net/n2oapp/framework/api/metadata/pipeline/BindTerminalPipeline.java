package net.n2oapp.framework.api.metadata.pipeline;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.util.SubModelsProcessor;

/**
 * Конвейер маппинга метаданных
 */
public interface BindTerminalPipeline extends Pipeline,
        BindProcessingPipeline<BindTerminalPipeline> {

    <D extends Compiled> D get(D input, CompileContext<?, ?> context, DataSet data);

    <D extends Compiled> D get(D input, CompileContext<?, ?> context, DataSet data, SubModelsProcessor subModelsProcessor);
}
