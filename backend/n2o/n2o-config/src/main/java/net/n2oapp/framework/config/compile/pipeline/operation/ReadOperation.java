package net.n2oapp.framework.config.compile.pipeline.operation;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.SourceMetadata;

import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.aware.PipelineOperationTypeAware;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperation;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperationType;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.reader.SourceLoaderFactory;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.config.register.route.RouteUtil;

import java.util.function.Supplier;

/**
 * Операция чтения метаданных в конвеере
 */
public class ReadOperation<S extends SourceMetadata, I> implements PipelineOperation<S, I>, PipelineOperationTypeAware, MetadataEnvironmentAware {

    private MetadataRegister configRegister;
    private SourceLoaderFactory readerFactory;

    public ReadOperation() {
    }

    public ReadOperation(MetadataRegister configRegister, SourceLoaderFactory readerFactory) {
        this.configRegister = configRegister;
        this.readerFactory = readerFactory;
    }

    @Override
    public S execute(CompileContext<?,?> context, DataSet data, Supplier<I> supplier, CompileProcessor compileProcessor,
                     BindProcessor bindProcessor,
                     SourceProcessor sourceProcessor) {
        String sourceId = context.getSourceId(bindProcessor);
        Class<S> sourceClass = (Class<S>) context.getSourceClass();
        SourceInfo info = configRegister.get(sourceId, sourceClass);
        return readerFactory.read(info, RouteUtil.parseQuery(sourceId));
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.configRegister = environment.getMetadataRegister();
        this.readerFactory = environment.getSourceLoaderFactory();
    }

    @Override
    public PipelineOperationType getPipelineOperationType() {
        return PipelineOperationType.READ;
    }
}
