package net.n2oapp.framework.config.reader;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.config.register.XmlInfo;

/**
 * Читатель метаданных
 */
@Deprecated
public interface ConfigReader extends SourceLoader<XmlInfo> {

    @Override
    default <S extends SourceMetadata> S load(XmlInfo info, String params) {
        return read(info.getId(), (Class<S>)info.getBaseSourceClass(), info.getContext());
    }

    @Deprecated
    <T extends SourceMetadata> T read(String id, Class<T> metadataClass, CompileContext context);
    @Deprecated
    <T extends SourceMetadata> T read(String id, String src, Class<T> metadataClass);
}
