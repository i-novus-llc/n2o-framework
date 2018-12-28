package net.n2oapp.framework.api.metadata.local;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;

/**
 * Получение исходных метаданных
 */
@Deprecated //use CompileProcessor
public interface GlobalMetadataProvider {

    <S extends SourceMetadata> S getGlobal(String id, Class<S> n2oClass);

    <T extends SourceMetadata, D extends CompileContext> D getContext(String id, Class<T> n2oClass);


}
