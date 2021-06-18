package net.n2oapp.framework.api.metadata.meta.action.modal.open_drawer;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.modal.AbstractModal;

/**
 * Клиентская модель открытия drawer окна
 */
@Getter
@Setter
public class OpenDrawer extends AbstractModal<OpenDrawerPayload> {

    public OpenDrawer() {
        super(new OpenDrawerPayload(), null);
    }
}
