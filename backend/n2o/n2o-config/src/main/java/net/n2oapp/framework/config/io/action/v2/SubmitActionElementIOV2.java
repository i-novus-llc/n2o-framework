package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.event.action.N2oSubmitAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись действия сохранения источника данных
 */
@Component
public class SubmitActionElementIOV2 extends AbstractActionElementIOV2<N2oSubmitAction> {

    @Override
    public void io(Element e, N2oSubmitAction a, IOProcessor p) {
        super.io(e, a, p);
        p.attribute(e, "datasource", a::getDatasourceId, a::setDatasourceId);
    }

    @Override
    public String getElementName() {
        return "submit";
    }

    @Override
    public Class<N2oSubmitAction> getElementClass() {
        return N2oSubmitAction.class;
    }
}
