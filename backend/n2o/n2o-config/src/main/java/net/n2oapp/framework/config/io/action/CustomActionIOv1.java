package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.event.action.N2oCustomAction;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись кастомного обработчика действий
 */
@Component
public class CustomActionIOv1 extends AbstractActionElementIOV1<N2oCustomAction> {

    @Override
    public String getElementName() {
        return "custom";
    }

    @Override
    public Class<N2oCustomAction> getElementClass() {
        return N2oCustomAction.class;
    }
}
