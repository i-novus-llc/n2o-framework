package net.n2oapp.framework.config.compile.pipeline.operation;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.aware.PipelineOperationTypeAware;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperation;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperationType;
import net.n2oapp.framework.api.metadata.validate.SourceValidatorFactory;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;

import java.util.function.Supplier;

/**
 * Операция по валидации метаданных в конвейере
 */
public class ValidateOperation<S> implements PipelineOperation<S, S>, PipelineOperationTypeAware, MetadataEnvironmentAware {

    private SourceValidatorFactory sourceValidatorFactory;

    public ValidateOperation() {
    }

    public ValidateOperation(SourceValidatorFactory sourceValidatorFactory) {
        this.sourceValidatorFactory = sourceValidatorFactory;
    }

    @Override
    public S execute(CompileContext<?,?> context, DataSet data, Supplier<S> supplier, CompileProcessor compileProcessor,
                     BindProcessor bindProcessor,
                     SourceProcessor sourceProcessor) {
        S value = supplier.get();
        sourceValidatorFactory.validate(value, sourceProcessor);
        return value;
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.sourceValidatorFactory = environment.getSourceValidatorFactory();
    }

    @Override
    public PipelineOperationType getPipelineOperationType() {
        return PipelineOperationType.VALIDATE;
    }
}
