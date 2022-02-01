package net.n2oapp.framework.api.metadata.meta.action.alert;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель действия оповещения
 */
@Getter
@Setter
public class AlertAction extends AbstractAction<ActionPayload, MetaSaga> {
    public AlertAction() {
        super(null, new MetaSaga());
    }
}
