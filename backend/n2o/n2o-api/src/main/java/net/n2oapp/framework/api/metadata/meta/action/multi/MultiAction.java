package net.n2oapp.framework.api.metadata.meta.action.multi;

import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

import java.util.List;

/**
 * Клиентская модель последовательности действий
 */
public class MultiAction extends AbstractAction<MultiActionPayload, MetaSaga> {

    public MultiAction(List<Action> actions) {
        super(new MultiActionPayload(actions), null);
    }
}
