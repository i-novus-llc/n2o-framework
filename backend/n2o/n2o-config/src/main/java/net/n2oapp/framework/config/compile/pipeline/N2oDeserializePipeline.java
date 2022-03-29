package net.n2oapp.framework.config.compile.pipeline;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.pipeline.DeserializePersistTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.DeserializePipeline;
import net.n2oapp.framework.api.metadata.pipeline.DeserializeTerminalPipeline;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static net.n2oapp.framework.api.metadata.pipeline.PipelineOperationType.*;

public class N2oDeserializePipeline extends N2oPipeline implements DeserializePipeline {

    protected N2oDeserializePipeline(MetadataEnvironment env) {
        super(env);
    }

    @Override
    public DeserializeTerminalPipeline<DeserializePersistTerminalPipeline> deserialize() {
        pullOp(DESERIALIZE);
        return new DeserializeTerminalPipeline<>() {
            @Override
            public <S extends SourceMetadata> S get(InputStream json, Class<S> sourceClass) {
                return execute(new DummyCompileContext<>("undefined", sourceClass), null, json);
            }

            @Override
            public DeserializePersistTerminalPipeline persist() {
                pullOp(PERSIST);
                return new DeserializePersistTerminalPipeline() {
                    @Override
                    public <S extends SourceMetadata> InputStream get(InputStream json, Class<S> sourceClass) {
                        return execute(new DummyCompileContext<>("undefined", sourceClass), null, json);
                    }

                    @Override
                    public <S extends SourceMetadata> void set(InputStream json, Class<S> sourceClass, OutputStream output) {
                        try (InputStream is = get(json, sourceClass)) {
                            IOUtils.copy(is, output);
                        } catch (IOException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                };
            }

            @Override
            public DeserializeTerminalPipeline<DeserializePersistTerminalPipeline> validate() {
                pullOp(VALIDATE);
                return this;
            }

            @Override
            public DeserializeTerminalPipeline<DeserializePersistTerminalPipeline> merge() {
                pullOp(MERGE);
                return this;
            }

            @Override
            public DeserializeTerminalPipeline<DeserializePersistTerminalPipeline> transform() {
                pullOp(SOURCE_TRANSFORM);
                return this;
            }

            @Override
            public DeserializeTerminalPipeline<DeserializePersistTerminalPipeline> cache() {
                pullOp(SOURCE_CACHE);
                return this;
            }

            @Override
            public DeserializeTerminalPipeline<DeserializePersistTerminalPipeline> copy() {
                pullOp(COPY);
                return this;
            }
        };
    }

}
