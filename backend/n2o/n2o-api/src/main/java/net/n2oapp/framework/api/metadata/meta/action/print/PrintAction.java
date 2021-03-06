package net.n2oapp.framework.api.metadata.meta.action.print;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель действия печати
 */
@Getter
@Setter
public class PrintAction extends AbstractAction<PrintActionPayload, MetaSaga> {

    public PrintAction() {
        super(new PrintActionPayload(), null);
    }
}
