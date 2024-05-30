package net.n2oapp.framework.config.io.page.v3;

import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBasePage;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import net.n2oapp.framework.config.io.region.v2.RegionIOv2;
import net.n2oapp.framework.config.io.toolbar.ToolbarIO;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Чтение\запись базовой страницы версии 3.0
 */
public abstract class BasePageElementIOv3<T extends N2oBasePage> implements NamespaceIO<T> {
    private Namespace regionDefaultNamespace = RegionIOv2.NAMESPACE;
    private Namespace pageDefaultNamespace = PageIOv3.NAMESPACE;
    private Namespace actionDefaultNamespace = ActionIOv1.NAMESPACE;

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
        p.attributeBoolean(e, "show-title", m::getShowTitle, m::setShowTitle);
        p.children(e, "actions", "action", m::getActions, m::setActions, ActionBar::new, this::action);
        p.children(e, null, "toolbar", m::getToolbars, m::setToolbars, new ToolbarIO());
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
    }

    private void action(Element e, ActionBar a, IOProcessor p) {
        p.attribute(e, "id", a::getId, a::setId);
        p.anyChildren(e, null, a::getN2oActions, a::setN2oActions, p.anyOf(N2oAction.class), actionDefaultNamespace);
    }

    @Override
    public String getNamespaceUri() {
        return pageDefaultNamespace.getURI();
    }

    public Namespace getRegionDefaultNamespace() {
        return regionDefaultNamespace;
    }

    public void setRegionDefaultNamespace(String regionDefaultNamespace) {
        this.regionDefaultNamespace = Namespace.getNamespace(regionDefaultNamespace);
    }
}
