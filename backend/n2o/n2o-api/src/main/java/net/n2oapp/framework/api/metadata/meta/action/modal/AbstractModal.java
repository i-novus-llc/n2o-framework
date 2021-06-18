package net.n2oapp.framework.api.metadata.meta.action.modal;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель открытия окна
 */
@Getter
@Setter
public abstract class AbstractModal<T extends ModalPayload> extends AbstractAction<T, MetaSaga> {
    private String objectId;
    private String operationId;
    private String pageId;

    public AbstractModal(T payload, MetaSaga meta) {
        super(payload, meta);
    }
}
