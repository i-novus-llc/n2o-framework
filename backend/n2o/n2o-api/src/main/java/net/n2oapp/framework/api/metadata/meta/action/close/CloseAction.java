package net.n2oapp.framework.api.metadata.meta.action.close;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractReduxAction;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель close-action
 */
@Getter
@Setter
public class CloseAction extends AbstractReduxAction<ActionPayload, MetaSaga> {

    @JsonProperty
    private Boolean prompt;

    public CloseAction() {
        super(null, null);
    }
}
