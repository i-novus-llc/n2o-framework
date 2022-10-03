package net.n2oapp.framework.config.io.page.v4;


import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBreadcrumb;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Чтение\запись абстрактной страницы версии 4.0
 */
public abstract class AbstractPageElementIOv4<T extends N2oPage> implements NamespaceIO<T> {
    private static final Namespace pageDefaultNamespace = PageIOv4.NAMESPACE;

    @Override
    public void io(Element e, T m, IOProcessor p) {
        p.attribute(e, "name", m::getName, m::setName);
        p.attribute(e, "title", m::getTitle, m::setTitle);
        p.attribute(e, "html-title", m::getHtmlTitle, m::setHtmlTitle);
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.attribute(e, "class", m::getCssClass, m::setCssClass);
        p.attribute(e, "style", m::getStyle, m::setStyle);
        p.attribute(e, "object-id", m::getObjectId, m::setObjectId);
        p.attribute(e, "route", m::getRoute, m::setRoute);
        p.attribute(e, "modal-size", m::getModalSize, m::setModalSize);
        p.attributeBoolean(e, "show-title", m::getShowTitle, m::setShowTitle);
        p.attributeEnum(e, "model", m::getModel, m::setModel, ReduxModel.class);
        p.hasElement(e, "breadcrumbs", m::getHasBreadcrumbs, m::setHasBreadcrumbs);
        p.children(e, "breadcrumbs", "crumb", m::getBreadcrumbs, m::setBreadcrumbs,
                N2oBreadcrumb.class, this::breadcrumbs);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
    }

    private void breadcrumbs(Element e, N2oBreadcrumb c, IOProcessor p) {
        p.attribute(e, "label", c::getLabel, c::setLabel);
        p.attribute(e, "path", c::getPath, c::setPath);
    }

    @Override
    public String getNamespaceUri() {
        return pageDefaultNamespace.getURI();
    }
}
