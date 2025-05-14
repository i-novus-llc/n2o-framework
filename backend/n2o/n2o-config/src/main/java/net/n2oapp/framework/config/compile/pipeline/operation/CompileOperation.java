package net.n2oapp.framework.config.compile.pipeline.operation;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.aware.PipelineOperationTypeAware;
import net.n2oapp.framework.api.metadata.compile.*;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperation;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperationTypeEnum;

import java.util.function.Supplier;

/**
 * Операция по сборке метаданных в конвейере
 */
public class CompileOperation<D extends Compiled, S> implements PipelineOperation<D, S>, PipelineOperationTypeAware, MetadataEnvironmentAware {

    private SourceCompilerFactory sourceCompilerFactory;

    public CompileOperation() {
    }

    public CompileOperation(SourceCompilerFactory sourceCompilerFactory) {
        this.sourceCompilerFactory = sourceCompilerFactory;
    }

    @Override
    public D execute(CompileContext<?, ?> context, DataSet data, Supplier<S> supplier, CompileProcessor compileProcessor,
                     BindProcessor bindProcessor,
                     SourceProcessor sourceProcessor) {
        S value = supplier.get();
        return sourceCompilerFactory.compile(value, context, compileProcessor);
    }

    @Override
    public PipelineOperationTypeEnum getPipelineOperationType() {
        return PipelineOperationTypeEnum.COMPILE;
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.sourceCompilerFactory = environment.getSourceCompilerFactory();
    }
}
