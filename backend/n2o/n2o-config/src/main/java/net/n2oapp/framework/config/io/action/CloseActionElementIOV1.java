package net.n2oapp.framework.config.io.action;


import net.n2oapp.framework.api.metadata.action.N2oCloseAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись закрытия окна
 */
@Component
public class CloseActionElementIOV1 extends AbstractActionElementIOV1<N2oCloseAction> {

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
