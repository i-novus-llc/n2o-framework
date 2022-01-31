package net.n2oapp.framework.config.io.action.v2;


import net.n2oapp.framework.api.metadata.event.action.N2oCloseAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись закрытия окна версии 2.0
 */
@Component
public class CloseActionElementIOV2 extends AbstractActionElementIOV2<N2oCloseAction> {

    @Override
    public void io(Element e, N2oCloseAction a, IOProcessor p) {
        super.io(e, a, p);
        p.attributeBoolean(e, "unsaved-data-prompt", a::getPrompt, a::setPrompt);
        p.attributeBoolean(e, "refresh", a::getRefresh, a::setRefresh);
    }

    @Override
    public String getElementName() {
        return "close";
    }

    @Override
    public Class<N2oCloseAction> getElementClass() {
        return N2oCloseAction.class;
    }
}
