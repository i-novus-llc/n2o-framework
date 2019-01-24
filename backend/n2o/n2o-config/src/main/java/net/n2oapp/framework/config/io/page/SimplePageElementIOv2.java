package net.n2oapp.framework.config.io.page;

import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.widget.WidgetIOv4;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись страницы с одним виджетом версии 2.0
 */
@Component
public class SimplePageElementIOv2 implements NamespaceIO<N2oSimplePage> {
    private Namespace widgetDefaultNamespace = WidgetIOv4.NAMESPACE;

    @Override
    public void io(Element e, N2oSimplePage m, IOProcessor p) {
        p.attribute(e, "name", m::getName, m::setName);
        p.attribute(e, "route", m::getRoute, m::setRoute);
        p.attribute(e, "modal-size", m::getModalSize, m::setModalSize);
        p.extensionAttributes(e, m::getExtAttributes, m::setExtAttributes);
        p.anyChild(e,null, m::getWidget, m::setWidget, p.anyOf(N2oWidget.class), widgetDefaultNamespace);
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
        return "http://n2oapp.net/framework/config/schema/page-2.0";
    }

    public void setWidgetDefaultNamespace(String widgetDefaultNamespace) {
        this.widgetDefaultNamespace = Namespace.getNamespace(widgetDefaultNamespace);
    }


}
