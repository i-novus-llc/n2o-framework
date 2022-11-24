package net.n2oapp.framework.api.metadata.meta.action.condition;

import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель условного действия
 */
public class ConditionAction extends AbstractAction<ConditionActionPayload, MetaSaga> {
    public ConditionAction() {
        super(new ConditionActionPayload(), null);
    }
}
