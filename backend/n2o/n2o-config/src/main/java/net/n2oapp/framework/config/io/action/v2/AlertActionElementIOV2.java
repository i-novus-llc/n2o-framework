package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.event.action.N2oAlertAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;


/**
 * Чтение/запись действия уведомления версии 2.0
 */
@Component
public class AlertActionElementIOV2 extends AbstractActionElementIOV2<N2oAlertAction> {

    @Override
    public void io(Element e, N2oAlertAction a, IOProcessor p) {
        super.io(e, a, p);
        p.attribute(e, "title", a::getTitle, a::setTitle);
        p.attribute(e, "text", a::getText, a::setText);
        p.attribute(e, "href", a::getHref, a::setHref);
        p.attribute(e, "color", a::getColor, a::setColor);
        p.attribute(e, "style", a::getStyle, a::setStyle);
        p.attribute(e, "class", a::getCssClass, a::setCssClass);
        p.attribute(e, "time", a::getTime, a::setTime);
        p.attribute(e, "timeout", a::getTimeout, a::setTimeout);
        p.attribute(e, "placement", a::getPlacement, a::setPlacement);
        p.attributeBoolean(e, "close-button", a::getCloseButton, a::setCloseButton);
    }

    @Override
    public Class<N2oAlertAction> getElementClass() {
        return N2oAlertAction.class;
    }

    @Override
    public String getElementName() {
        return "alert";
    }
}
