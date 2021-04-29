package net.n2oapp.framework.api.metadata.meta.action.modal.show_modal;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.modal.AbstractModal;

/**
 * Клиентская модель открытия модального окна
 */
@Getter
@Setter
public class ShowModal extends AbstractModal<ShowModalPayload> {

    public ShowModal() {
        super(new ShowModalPayload(), null);
    }
}
