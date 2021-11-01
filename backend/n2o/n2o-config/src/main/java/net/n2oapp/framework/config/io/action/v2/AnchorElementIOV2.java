package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись действия открытия страницы браузера версии 2.0
 */
@Component
public class AnchorElementIOV2 extends AbstractActionElementIOV2<N2oAnchor> {
    @Override
    public void io(Element e, N2oAnchor a, IOProcessor p) {
        super.io(e, a, p);
        p.attribute(e,"href", a::getHref,a::setHref);
        p.attributeEnum(e,"target",a::getTarget,a::setTarget, Target.class);
        p.children(e, null, "path-param", a::getPathParams, a::setPathParams, N2oParam::new, this::param);
        p.children(e, null, "query-param", a::getQueryParams, a::setQueryParams, N2oParam::new, this::param);
    }

    private void param(Element e, N2oParam param, IOProcessor p) {
        p.attribute(e, "name", param::getName, param::setName);
        p.attribute(e, "value", param::getValue, param::setValue);
        p.attribute(e, "ref-datasource", param::getDatasource, param::setDatasource);
        p.attributeEnum(e, "ref-model", param::getModel, param::setModel, ReduxModel.class);
    }

    @Override
    public String getElementName() {
        return "a";
    }

    @Override
    public Class<N2oAnchor> getElementClass() {
        return N2oAnchor.class;
    }
}
