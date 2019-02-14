package net.n2oapp.framework.api.metadata.global;

import net.n2oapp.framework.api.metadata.local.N2oMetadataMerger;

/**
 * Метаданные, на которые можно сослаться по ссылке
 */
@Deprecated
public interface N2oReference<T extends N2oMetadata> {
    /**
     * Ссылка на метаданную
     */
    String getRefId();

    /**
     * Сервис слияния метаданной по ссылке, с метаданной заданной inline
     */
    N2oMetadataMerger<T> getMerger();
}
