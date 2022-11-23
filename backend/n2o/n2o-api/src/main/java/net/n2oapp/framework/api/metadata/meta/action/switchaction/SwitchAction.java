package net.n2oapp.framework.api.metadata.meta.action.switchaction;

import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель действия switch
 */
public class SwitchAction extends AbstractAction<SwitchActionPayload, MetaSaga> {
    public SwitchAction() {
        super(new SwitchActionPayload(), null);
    }
}
