package net.n2oapp.framework.api.metadata.meta.action.print;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель печати
 */
@Getter
@Setter
public class Print extends AbstractAction<PrintPayload, MetaSaga> {

    public Print() {
        super(new PrintPayload(), null);
    }
}
