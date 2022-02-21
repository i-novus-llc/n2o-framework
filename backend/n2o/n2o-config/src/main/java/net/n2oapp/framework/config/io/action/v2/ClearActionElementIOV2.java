package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.event.action.N2oClearAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись очистки модели
 */
@Component
public class ClearActionElementIOV2 extends AbstractActionElementIOV2<N2oClearAction> {

    @Override
    public void io(Element e, N2oClearAction a, IOProcessor p) {
        super.io(e, a, p);
        p.attribute(e, "datasource", a::getDatasourceId, a::setDatasourceId);
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
