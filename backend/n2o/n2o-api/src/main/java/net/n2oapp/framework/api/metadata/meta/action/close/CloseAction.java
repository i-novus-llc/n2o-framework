package net.n2oapp.framework.api.metadata.meta.action.close;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель close-action
 */
@Getter
@Setter
public class CloseAction extends AbstractAction<ActionPayload, MetaSaga> {
    public CloseAction() {
        super(null, null);
    }
}
