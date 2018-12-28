package net.n2oapp.framework.config.io.action;


import net.n2oapp.framework.api.metadata.event.action.N2OValidateAction;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись  элемента  валидации виджета
 */
@Component
public class N2oValidateActionElementIOV1 extends AbstractActionElementIOV1<N2OValidateAction> {

    @Override
    public String getElementName() {
        return "perform-validation";
    }

    @Override
    public Class<N2OValidateAction> getElementClass() {
        return N2OValidateAction.class;
    }
}
