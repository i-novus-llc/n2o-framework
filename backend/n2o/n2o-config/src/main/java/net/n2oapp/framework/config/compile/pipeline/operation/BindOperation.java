package net.n2oapp.framework.config.compile.pipeline.operation;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.aware.PipelineOperationTypeAware;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.MetadataBinderFactory;
import net.n2oapp.framework.api.metadata.compile.CompileContext;

import net.n2oapp.framework.api.metadata.pipeline.PipelineOperation;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperationType;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;

import java.util.function.Supplier;

/**
 * Операция связывания с данными метаданных в конвейере
 */
public class BindOperation<D extends Compiled> implements PipelineOperation<D, D>, PipelineOperationTypeAware, MetadataEnvironmentAware {

    private MetadataBinderFactory binderFactory;

    public BindOperation() {
    }

    public BindOperation(MetadataBinderFactory binderFactory) {
        this.binderFactory = binderFactory;
    }

    @Override
    public D execute(CompileContext<?,?> context, DataSet data, Supplier<D> supplier,
                     CompileProcessor compileProcessor,
                     BindProcessor bindProcessor,
                     SourceProcessor sourceProcessor) {
        return binderFactory.bind(supplier.get(), bindProcessor);
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.binderFactory = environment.getMetadataBinderFactory();
    }

    @Override
    public PipelineOperationType getPipelineOperationType() {
        return PipelineOperationType.BIND;
    }
}
