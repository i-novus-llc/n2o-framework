package net.n2oapp.framework.api.metadata.meta.action.set_value;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель set-value
 */

@Getter
@Setter
public class SetValueAction extends AbstractAction<SetValueActionPayload, MetaSaga> {
    @JsonProperty
    private Boolean validate;

    public SetValueAction() {
        super(new SetValueActionPayload(), null);
    }
}
