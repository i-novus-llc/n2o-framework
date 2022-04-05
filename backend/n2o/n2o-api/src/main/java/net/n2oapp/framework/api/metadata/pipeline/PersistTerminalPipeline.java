package net.n2oapp.framework.api.metadata.pipeline;

import net.n2oapp.framework.api.metadata.SourceMetadata;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Завершающий конвейер сохранения метаданных
 */
public interface PersistTerminalPipeline extends Pipeline  {
    <S extends SourceMetadata> InputStream get(S input);

    <S extends SourceMetadata> void set(S input, OutputStream output);

    <S extends SourceMetadata> void set(S input, String directory);
}
