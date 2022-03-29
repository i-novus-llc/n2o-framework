package net.n2oapp.framework.api.metadata.pipeline;

import net.n2oapp.framework.api.metadata.SourceMetadata;

import java.io.InputStream;
import java.io.OutputStream;

public interface DeserializePersistTerminalPipeline extends Pipeline {

    <S extends SourceMetadata> InputStream get(InputStream json, Class<S> sourceClass);

    <S extends SourceMetadata> void set(InputStream json, Class<S> sourceClass, OutputStream output);

}
