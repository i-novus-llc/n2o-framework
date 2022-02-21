package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.event.action.N2oRefreshAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись действия обновления данных виджета
 */
@Component
public class RefreshActionElementIOV2 extends AbstractActionElementIOV2<N2oRefreshAction> {

    @Override
    public void io(Element e, N2oRefreshAction a, IOProcessor p) {
        super.io(e, a, p);
        p.attribute(e, "datasource", a::getDatasourceId, a::setDatasource);
    }

    @Override
    public String getElementName() {
        return "refresh";
    }

    @Override
    public Class<N2oRefreshAction> getElementClass() {
        return N2oRefreshAction.class;
    }
}
