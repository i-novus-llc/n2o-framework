package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oPrintAction;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись действия печати
 */
@Component
public class PrintActionElementIOV2 extends AbstractActionElementIOV2<N2oPrintAction> {
    @Override
    public void io(Element e, N2oPrintAction a, IOProcessor p) {
        super.io(e, a, p);
        p.attribute(e,"url", a::getUrl,a::setUrl);
        p.children(e, null, "path-param", a::getPathParams, a::setPathParams, N2oParam::new, this::param);
        p.children(e, null, "query-param", a::getQueryParams, a::setQueryParams, N2oParam::new, this::param);
    }

    private void param(Element e, N2oParam param, IOProcessor p) {
        p.attribute(e, "name", param::getName, param::setName);
        p.attribute(e, "value", param::getValue, param::setValue);
        p.attributeEnum(e, "model", param::getModel, param::setModel, ReduxModel.class);
        p.attribute(e, "datasource", param::getDatasource, param::setDatasource);
    }

    @Override
    public String getElementName() {
        return "print";
    }

    @Override
    public Class<N2oPrintAction> getElementClass() {
        return N2oPrintAction.class;
    }
}
