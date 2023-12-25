package net.n2oapp.framework.api.metadata.meta.action.confirm;

import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

public class ConfirmAction extends AbstractAction<ConfirmActionPayload, MetaSaga> {

    public ConfirmAction() {
        super(new ConfirmActionPayload(), null);
    }
}
