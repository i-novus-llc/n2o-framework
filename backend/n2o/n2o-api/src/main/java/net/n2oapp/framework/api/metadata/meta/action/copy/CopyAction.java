package net.n2oapp.framework.api.metadata.meta.action.copy;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractReduxAction;
import net.n2oapp.framework.api.metadata.meta.saga.AsyncMetaSaga;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель вызова запросов
 */
@Getter
@Setter
public class CopyAction extends AbstractReduxAction<CopyActionPayload, MetaSaga> {

    public CopyAction() {
        super(new CopyActionPayload(), null);
    }
}
