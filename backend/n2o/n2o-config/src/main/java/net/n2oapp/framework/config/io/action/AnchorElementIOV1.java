package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись действия открытия страницы браузера
 */
@Component
public class AnchorElementIOV1 extends AbstractActionElementIOV1<N2oAnchor> {
    @Override
    public void io(Element e, N2oAnchor a, IOProcessor p) {
        super.io(e, a, p);
        p.attribute(e,"href", a::getHref,a::setHref);
        p.attributeEnum(e,"target",a::getTarget,a::setTarget, TargetEnum.class);
        p.children(e, null, "path-param", a::getPathParams, a::setPathParams, N2oParam::new, this::param);
        p.children(e, null, "query-param", a::getQueryParams, a::setQueryParams, N2oParam::new, this::param);
    }

    private void param(Element e, N2oParam param, IOProcessor p) {
        p.attribute(e, "name", param::getName, param::setName);
        p.attribute(e, "value", param::getValue, param::setValue);
        p.attribute(e, "ref-widget-id", param::getRefWidgetId, param::setRefWidgetId);
        p.attributeEnum(e, "ref-model", param::getModel, param::setModel, ReduxModelEnum.class);
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
