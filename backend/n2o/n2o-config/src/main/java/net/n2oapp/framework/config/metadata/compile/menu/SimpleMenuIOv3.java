package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.event.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.event.action.N2oOpenPage;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись меню 3.0
 */
@Component
public class SimpleMenuIOv3 implements NamespaceIO<N2oSimpleMenu> {

    private final Namespace actionDefaultNamespace = ActionIOv1.NAMESPACE;

    @Override
    public Class<N2oSimpleMenu> getElementClass() {
        return N2oSimpleMenu.class;
    }

    @Override
    public String getElementName() {
        return "menu";
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/menu-3.0";
    }

    @Override
    public void io(Element e, N2oSimpleMenu m, IOProcessor p) {
        p.anyChildren(e, null, m::getMenuItems, m::setMenuItems, p.oneOf(N2oSimpleMenu.MenuItem.class)
                .add("menu-item", N2oSimpleMenu.MenuItem.class, this::menuItem)
                .add("dropdown-menu", N2oSimpleMenu.SubMenuItem.class, this::dropDownMenu));
    }

    private void menuItem(Element e, N2oSimpleMenu.MenuItem m, IOProcessor p) {
        p.attribute(e, "id", m::getId, m::setId);
        p.attribute(e, "name", m::getLabel, m::setLabel);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attribute(e, "badge-color", m::getBadgeColor, m::setBadgeColor);
        p.attributeInteger(e, "badge", m::getBadge, m::setBadge);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
        p.anyChild(e, null, m::getAction, m::setAction, p.oneOf(N2oSimpleMenu.MenuItem.class)
                .add("open-page", N2oSimpleMenu.PageItem.class, this::page)
                .add("a", N2oSimpleMenu.AnchorItem.class, this::anchor));
    }

    private void page(Element e, N2oSimpleMenu.MenuItem m, IOProcessor p) {
        p.attribute(e, "page-id", m::getPageId, m::setPageId);
        p.attribute(e, "route", m::getRoute, m::setRoute);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
    }

    private void anchor(Element e, N2oSimpleMenu.MenuItem m, IOProcessor p) {
        p.attribute(e, "href", m::getHref, m::setHref);
        p.attributeEnum(e, "target", m::getTarget, m::setTarget, Target.class);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
    }


    private void dropDownMenu(Element e, N2oSimpleMenu.SubMenuItem m, IOProcessor p) {
        p.attribute(e, "id", m::getId, m::setId);
        p.attribute(e, "name", m::getName, m::setName);
        p.attribute(e, "image", m::getImage, m::setImage);
        p.attributeEnum(e, "image-shape", m::getImageShape, m::setImageShape, ImageShape.class);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
        p.anyChildren(e, null, m::getMenuItems, m::setMenuItems, p.oneOf(N2oSimpleMenu.MenuItem.class)
                .add("menu-item", N2oSimpleMenu.MenuItem.class, this::menuItem)
                .add("divider", N2oSimpleMenu.DividerItem.class, this::divider));
    }

    private void divider(Element e, N2oSimpleMenu.DividerItem m, IOProcessor p) {
    }
}
