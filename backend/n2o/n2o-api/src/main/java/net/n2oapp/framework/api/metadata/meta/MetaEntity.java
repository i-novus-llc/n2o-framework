package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.n2oapp.framework.api.metadata.ClientMetadata;

/**
 * Асбтрактная реализация клиентских метаданных
 */
@Deprecated
public abstract class MetaEntity implements ClientMetadata {
    @JsonProperty
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean isProcessable() {
        return true;
    }

}
