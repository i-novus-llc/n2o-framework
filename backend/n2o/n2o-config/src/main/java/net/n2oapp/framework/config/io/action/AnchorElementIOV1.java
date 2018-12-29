package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.event.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
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
        p.attributeEnum(e,"target",a::getTarget,a::setTarget, Target.class);
        p.children(e, null, "path-param", a::getPathParams, a::setPathParams, N2oAnchor.Param::new, this::param);
        p.children(e, null, "query-param", a::getQueryParams, a::setQueryParams, N2oAnchor.Param::new, this::param);
    }

    @Override
    public String getElementName() {
        return "a";
    }

    @Override
    public Class<N2oAnchor> getElementClass() {
        return N2oAnchor.class;
    }


    private void param(Element e, N2oAnchor.Param param, IOProcessor p) {
        p.attribute(e, "name", param::getName, param::setName);
        p.attribute(e, "value", param::getValue, param::setValue);
    }
}
