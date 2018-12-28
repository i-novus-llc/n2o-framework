package net.n2oapp.framework.config.io.action;


import net.n2oapp.framework.api.metadata.event.action.N2oCloseAction;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись закрытия окна
 */
@Component
public class CloseActionElementIOV1 extends AbstractActionElementIOV1<N2oCloseAction> {

    @Override
    public String getElementName() {
        return "close";
    }

    @Override
    public Class<N2oCloseAction> getElementClass() {
        return N2oCloseAction.class;
    }
}
