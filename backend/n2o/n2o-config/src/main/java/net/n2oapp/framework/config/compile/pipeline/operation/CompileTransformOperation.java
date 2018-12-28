package net.n2oapp.framework.config.compile.pipeline.operation;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.aware.PipelineOperationTypeAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileTransformerFactory;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperation;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperationType;

import java.util.function.Supplier;

/**
 * Операция по трансформации собранных метаданных в конвеере
 */
public class CompileTransformOperation<D extends Compiled> implements PipelineOperation<D, D>, PipelineOperationTypeAware, MetadataEnvironmentAware {

    private CompileTransformerFactory compileTransformerFactory;

    public CompileTransformOperation() {
    }

    public CompileTransformOperation(CompileTransformerFactory compileTransformerFactory) {
        this.compileTransformerFactory = compileTransformerFactory;
    }

    @Override
    public D execute(CompileContext<?, ?> context, DataSet data, Supplier<D> supplier, CompileProcessor processor) {
        D value = supplier.get();
        return compileTransformerFactory.transform(value, context, processor);
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.compileTransformerFactory = environment.getCompileTransformerFactory();
    }

    @Override
    public PipelineOperationType getPipelineOperationType() {
        return PipelineOperationType.COMPILE_TRANSFORM;
    }
}
