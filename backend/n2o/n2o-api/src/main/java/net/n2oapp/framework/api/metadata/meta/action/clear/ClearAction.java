package net.n2oapp.framework.api.metadata.meta.action.clear;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractReduxAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель вызова запросов
 */
@Getter
@Setter
public class ClearAction extends AbstractReduxAction<ClearActionPayload, MetaSaga> {
    public ClearAction() {
        super(new ClearActionPayload(), null);
    }
}
