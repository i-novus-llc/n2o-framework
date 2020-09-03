package net.n2oapp.framework.api.metadata.meta.action.invoke;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.saga.AsyncMetaSaga;

/**
 * Клиентская модель вызова запросов
 */
@Getter
@Setter
public class InvokeAction extends AbstractAction<InvokeActionPayload, AsyncMetaSaga> {

    private String objectId;
    private String operationId;

    public InvokeAction() {
        super(new InvokeActionPayload(), new AsyncMetaSaga());
    }
}
