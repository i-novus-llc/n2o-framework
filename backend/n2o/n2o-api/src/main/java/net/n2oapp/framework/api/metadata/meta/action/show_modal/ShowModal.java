package net.n2oapp.framework.api.metadata.meta.action.show_modal;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель открытия модального окна
 */
@Getter
@Setter
public class ShowModal extends AbstractAction<ShowModalPayload, MetaSaga> {

    private String objectId;
    private String operationId;
    private String pageId;

    public ShowModal() {
        super(new ShowModalPayload(), null);
    }
}
