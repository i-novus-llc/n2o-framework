package net.n2oapp.framework.config.compile.pipeline.operation;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.aware.PipelineOperationTypeAware;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.MetadataBinderFactory;
import net.n2oapp.framework.api.metadata.compile.CompileContext;

import net.n2oapp.framework.api.metadata.pipeline.PipelineOperation;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperationType;

import java.util.function.Supplier;

/**
 * Операция связывания с данными метаданных в конвеере
 */
public class BindOperation<D extends Compiled> implements PipelineOperation<D, D>, PipelineOperationTypeAware, MetadataEnvironmentAware {

    private MetadataBinderFactory binderFactory;

    public BindOperation() {
    }

    public BindOperation(MetadataBinderFactory binderFactory) {
        this.binderFactory = binderFactory;
    }

    @Override
    public D execute(CompileContext<?,?> context, DataSet data, Supplier<D> supplier, CompileProcessor processor) {
        return binderFactory.bind(supplier.get(), processor);
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
