package net.n2oapp.framework.config.io.page.v3;

import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.widget.v4.WidgetIOv4;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись страницы с единственным виджетом версии 3.0
 */
@Component
public class SimplePageElementIOv3 implements NamespaceIO<N2oSimplePage> {

    @Override
    public void io(Element e, N2oSimplePage m, IOProcessor p) {
        p.attribute(e, "name", m::getName, m::setName);
        p.attribute(e, "title", m::getTitle, m::setTitle);
        p.attributeBoolean(e, "show-title", m::getShowTitle, m::setShowTitle);
        p.attribute(e, "html-title", m::getHtmlTitle, m::setHtmlTitle);
        p.attribute(e, "route", m::getRoute, m::setRoute);
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.attribute(e, "class", m::getCssClass, m::setCssClass);
        p.attribute(e, "style", m::getStyle, m::setStyle);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
        p.anyChild(e, null, m::getWidget, m::setWidget,
                p.anyOf(N2oWidget.class), WidgetIOv4.NAMESPACE);
        m.setNamespaceUri(getNamespaceUri());
    }

    @Override
    public Class<N2oSimplePage> getElementClass() {
        return N2oSimplePage.class;
    }

    @Override
    public N2oSimplePage newInstance(Element element) {
        return new N2oSimplePage();
    }

    @Override
    public String getElementName() {
        return "simple-page";
    }

    @Override
    public String getNamespaceUri() {
        return PageIOv3.NAMESPACE.getURI();
    }
}
