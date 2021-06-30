package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись меню 1.0
 */
@Component
public class SimpleMenuIOv2 implements NamespaceIO<N2oSimpleMenu> {

    @Override
    public Class<N2oSimpleMenu> getElementClass() {
        return N2oSimpleMenu.class;
    }

    @Override
    public String getElementName() {
        return "nav";
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/menu-2.0";
    }

    @Override
    public void io(Element e, N2oSimpleMenu m, IOProcessor p) {
        p.attribute(e, "ref-id", m::getRefId, m::setRefId);
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.anyChildren(e, null, m::getMenuItems, m::setMenuItems, p.oneOf(N2oSimpleMenu.MenuItem.class)
                .add("page", N2oSimpleMenu.PageItem.class, this::page)
                .add("a", N2oSimpleMenu.AnchorItem.class, this::anchor)
                .add("sub-menu", N2oSimpleMenu.SubMenuItem.class, this::subMenu));
    }

    private void page(Element e, N2oSimpleMenu.MenuItem m, IOProcessor p) {
        p.attribute(e, "label", m::getLabel, m::setLabel);
        p.attribute(e, "page-id", m::getPageId, m::setPageId);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attribute(e, "route", m::getRoute, m::setRoute);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
    }

    private void anchor(Element e, N2oSimpleMenu.MenuItem m, IOProcessor p) {
        p.attribute(e, "label", m::getLabel, m::setLabel);
        p.attribute(e, "href", m::getHref, m::setHref);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attributeEnum(e, "target", m::getTarget, m::setTarget, Target.class);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
    }

    private void subMenu(Element e, N2oSimpleMenu.SubMenuItem m, IOProcessor p) {
        p.attribute(e, "label", m::getLabel, m::setLabel);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attribute(e, "image", m::getImage, m::setImage);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
        p.anyChildren(e, null, m::getSubMenu, m::setSubMenu, p.oneOf(N2oSimpleMenu.MenuItem.class)
                .add("page", N2oSimpleMenu.PageItem.class, this::page)
                .add("a", N2oSimpleMenu.AnchorItem.class, this::anchor)
                .add("sub-menu", N2oSimpleMenu.SubMenuItem.class, this::subMenu));
    }
}
