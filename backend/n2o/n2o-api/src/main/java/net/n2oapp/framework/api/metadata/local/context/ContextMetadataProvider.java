package net.n2oapp.framework.api.metadata.local.context;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.local.GlobalMetadataProvider;

import java.io.Serializable;

/**
 * Служит для восстановления метаданных по родительскому контексту
 * Например, восстановить inline виджет зная контекст страницы
 */
@FunctionalInterface
public interface ContextMetadataProvider<T extends SourceMetadata, P extends CompileContext> extends Serializable {
    /**
     * Восстановление метаданной по родительскому контексту и провайдеру исходных метаданных
     * @param parentContext - родительский контекст
     * @param provider - провайдер исходных метаданных
     * @return метаданная
     */
    T provide(P parentContext, GlobalMetadataProvider provider);

    default T provide(P parentContext, CompileContext thisContext, GlobalMetadataProvider provider) {
        return provide(parentContext, provider);
    }
}
