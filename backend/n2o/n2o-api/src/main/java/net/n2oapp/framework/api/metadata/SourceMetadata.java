package net.n2oapp.framework.api.metadata;

import net.n2oapp.framework.api.metadata.aware.IdAware;

import java.io.Serializable;

/**
 * Метаданная считанная из исходника (например, из xml)
 */
public interface SourceMetadata extends Source, IdAware {
    /**
     * Идентификатор метаданной
     */
    String getId();

    /**
     * Установить идентификатор метаданной
     * @param id Идентификатор метаданной
     */
    void setId(String id);

    /**
     * Тип метаданной
     */
    default String getMetadataType() {
        return getPostfix();
    }

    @Deprecated
    String getPostfix();

    default Class<? extends SourceMetadata> getSourceBaseClass() {
        return getClass();
    }
}
