package net.n2oapp.framework.api.metadata.pipeline;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.register.MetaType;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Завершающий конвейер сохранения метаданных после чтения
 */
public interface ReadPersistTerminalPipeline extends Pipeline {
    <S extends SourceMetadata> InputStream get(String id, Class<S> sourceClass);

    <S extends SourceMetadata> void set(String id, Class<S> sourceClass, OutputStream output);

    <S extends SourceMetadata> void set(String id, MetaType metaType, String directory);
}
