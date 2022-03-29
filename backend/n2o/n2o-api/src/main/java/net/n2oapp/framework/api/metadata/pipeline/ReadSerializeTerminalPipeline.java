package net.n2oapp.framework.api.metadata.pipeline;

import net.n2oapp.framework.api.metadata.SourceMetadata;

import java.io.InputStream;
import java.io.OutputStream;

public interface ReadSerializeTerminalPipeline extends Pipeline {

    <S extends SourceMetadata> InputStream get(String id, Class<S> sourceClass);

    <S extends SourceMetadata> void set(String id, Class<S> sourceClass, OutputStream output);
}
