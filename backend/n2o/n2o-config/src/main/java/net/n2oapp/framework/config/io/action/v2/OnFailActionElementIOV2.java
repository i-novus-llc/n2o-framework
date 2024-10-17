package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.action.N2oOnFailAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись действий после падения ошибки
 */
@Component
public class OnFailActionElementIOV2 extends AbstractActionElementIOV2<N2oOnFailAction> {
    @Override
    public Class<N2oOnFailAction> getElementClass() {
        return N2oOnFailAction.class;
    }

    @Override
    public String getElementName() {
        return "on-fail";
    }

    @Override
    public void io(Element e, N2oOnFailAction a, IOProcessor p) {
        super.io(e, a, p);
        p.anyChildren(e, null, a::getActions, a::setActions, p.anyOf(N2oAction.class), ActionIOv2.NAMESPACE);
    }
}
