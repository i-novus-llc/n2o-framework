package net.n2oapp.framework.api.metadata.meta.action.copy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель вызова запросов
 */
@Getter
@Setter
public class CopyAction extends AbstractAction<CopyActionPayload, MetaSaga> {
    @JsonProperty
    private Boolean validate;

    public CopyAction() {
        super(new CopyActionPayload(), null);
    }
}
