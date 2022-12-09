package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.action.N2oClearAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись очистки модели
 */
@Component
public class ClearActionElementIOV1 extends AbstractActionElementIOV1<N2oClearAction> {

    @Override
    public void io(Element e, N2oClearAction a, IOProcessor p) {
        super.io(e, a, p);
        p.attributeArray(e, "model", ",", a::getModel, a::setModel);
        p.attributeBoolean(e, "close-on-success", a::getCloseOnSuccess, a::setCloseOnSuccess);
    }

    @Override
    public String getElementName() {
        return "clear";
    }

    @Override
    public Class<N2oClearAction> getElementClass() {
        return N2oClearAction.class;
    }
}
