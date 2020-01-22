package net.n2oapp.framework.api.metadata.meta.action.copy;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель вызова запросов
 */
@Getter
@Setter
public class CopyAction extends AbstractAction<CopyActionPayload, MetaSaga> {

    public CopyAction() {
        super(new CopyActionPayload(), null);
    }
}
