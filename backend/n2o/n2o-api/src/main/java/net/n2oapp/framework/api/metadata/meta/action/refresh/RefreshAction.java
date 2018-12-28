package net.n2oapp.framework.api.metadata.meta.action.refresh;

import net.n2oapp.framework.api.metadata.meta.action.AbstractReduxAction;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель refresh-action
 */
public class RefreshAction extends AbstractReduxAction<ActionPayload, MetaSaga> {

    public RefreshAction() {
        super(new RefreshPayload(), null);
    }
}
