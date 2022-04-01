package net.n2oapp.framework.config.compile.pipeline.operation;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.aware.PipelineOperationTypeAware;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperation;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperationType;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

/**
 * Операция по десериализации json в исходные модели метаданных
 */
public class DeserializeOperation<D extends SourceMetadata> implements PipelineOperation<D, InputStream>, PipelineOperationTypeAware, MetadataEnvironmentAware {

    private ObjectMapper mapper;

    public DeserializeOperation() {
    }

    public DeserializeOperation(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public D execute(CompileContext<?, ?> context, DataSet data, Supplier<InputStream> supplier, CompileProcessor compileProcessor,
                     BindProcessor bindProcessor, SourceProcessor sourceProcessor) {
        InputStream value = supplier.get();
        try {
            return (D) mapper.readValue(value, context.getSourceClass());
        } catch (IOException e) {
            throw new N2oException("Error during deserialize json to " + context.getSourceClass(), e);
        }
    }

    @Override
    public PipelineOperationType getPipelineOperationType() {
        return PipelineOperationType.DESERIALIZE;
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.mapper = environment.getSerializeObjectMapper();
    }
}
