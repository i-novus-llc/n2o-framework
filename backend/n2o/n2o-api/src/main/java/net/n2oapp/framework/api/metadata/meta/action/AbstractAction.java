package net.n2oapp.framework.api.metadata.meta.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

import java.util.Map;

/**
 * Абстрактная реализация клиентской модели действия
 */
@Getter
@Setter
public abstract class AbstractAction<P extends ActionPayload, M extends MetaSaga> implements Action {

    @JsonProperty
    private String type;
    @JsonProperty
    private P payload;
    @JsonProperty
    private M meta;

    protected Map<String, Object> properties;

    public AbstractAction(P payload, M meta) {
        this.payload = payload;
        this.meta = meta;
    }
}
