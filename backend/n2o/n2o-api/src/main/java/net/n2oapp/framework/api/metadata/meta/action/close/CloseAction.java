package net.n2oapp.framework.api.metadata.meta.action.close;

import net.n2oapp.framework.api.metadata.meta.action.AbstractReduxAction;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель close-action
 */
public class CloseAction extends AbstractReduxAction<ActionPayload, MetaSaga> {

    public CloseAction() {
        super(null, null);
    }
}
