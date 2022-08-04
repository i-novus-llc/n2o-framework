package net.n2oapp.framework.api.metadata.meta.action.submit;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель действия сохранения источника данных
 */
@Getter
@Setter
public class SubmitAction extends AbstractAction<SubmitActionPayload, MetaSaga>{

    public SubmitAction() {
        super(new SubmitActionPayload(), null);
    }
}
