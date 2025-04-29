package net.n2oapp.framework.config.compile.pipeline;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.pipeline.PersistPipeline;
import net.n2oapp.framework.api.metadata.pipeline.PersistTerminalPipeline;
import net.n2oapp.framework.config.register.storage.PathUtil;
import net.n2oapp.framework.config.util.FileSystemUtil;
import org.apache.commons.io.IOUtils;

import java.io.*;

import static net.n2oapp.framework.api.metadata.pipeline.PipelineOperationTypeEnum.*;

public class N2oPersistPipeline extends N2oPipeline implements PersistPipeline {

    public N2oPersistPipeline(MetadataEnvironment env) {
        super(env);
    }

    @Override
    public PersistTerminalPipeline persist() {
        pullOp(PERSIST);
        return new PersistTerminalPipeline() {
            @Override
            public <S extends SourceMetadata> InputStream get(S input) {
                return execute(new DummyCompileContext<>(input.getId(), input.getSourceBaseClass()), null, input);
            }

            @Override
            public <S extends SourceMetadata> void set(S input, OutputStream output) {
                try (InputStream is = get(input)) {
                    IOUtils.copy(is, output);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }

            @Override
            public <S extends SourceMetadata> void set(S input, String directory) {
                String path = PathUtil.concatFileNameAndBasePath(input.getId() + "." + input.getMetadataType() + ".xml", directory);
                FileSystemUtil.saveContentToFile(get(input), new File(path));
            }
        };
    }

    @Override
    public PersistPipeline validate() {
        pullOp(VALIDATE);
        return this;
    }

    @Override
    public PersistPipeline merge() {
        pullOp(MERGE);
        return this;
    }

    @Override
    public PersistPipeline transform() {
        pullOp(SOURCE_TRANSFORM);
        return this;
    }

    @Override
    public PersistPipeline cache() {
        pullOp(SOURCE_CACHE);
        return this;
    }

    @Override
    public PersistPipeline copy() {
        pullOp(COPY);
        return this;
    }
}
