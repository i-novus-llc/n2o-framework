package net.n2oapp.framework.config.compile.pipeline.operation;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.function.Supplier;

/**
 * Операция по сериализации метаданных в конвейере
 */
public class SerializeOperation<S extends SourceMetadata> implements PipelineOperation<InputStream, S>, PipelineOperationTypeAware, MetadataEnvironmentAware {

    private ObjectMapper mapper;

    public SerializeOperation() {
    }

    public SerializeOperation(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public InputStream execute(CompileContext<?, ?> context, DataSet data, Supplier<S> supplier,
                               CompileProcessor compileProcessor,
                                BindProcessor bindProcessor,
                               SourceProcessor sourceProcessor) {
        S value = supplier.get();
        byte[] buf;
        try {
            buf = mapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new N2oException("Error during serialize metadata " + value.getId(), e);
        }
        return new ByteArrayInputStream(buf);
    }

    @Override
    public PipelineOperationType getPipelineOperationType() {
        return PipelineOperationType.SERIALIZE;
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.mapper = environment.getSerializeObjectMapper();
    }
}
