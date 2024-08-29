package net.n2oapp.framework.config.io.menu;

import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.meta.badge.Position;
import net.n2oapp.framework.config.io.action.v2.ActionIOv2;
import net.n2oapp.framework.config.io.common.BadgeAwareIO;
import org.jdom2.Element;

/**
 * Чтение/запись меню 3.0
 */
public abstract class SimpleMenuIOv3 implements NamespaceIO<N2oSimpleMenu>, BadgeAwareIO<N2oSimpleMenu.MenuItem> {

    @Override
    public Class<N2oSimpleMenu> getElementClass() {
        return N2oSimpleMenu.class;
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/menu-3.0";
    }

    @Override
    public void io(Element e, N2oSimpleMenu m, IOProcessor p) {
        p.attribute(e, "ref-id", m::getRefId, m::setRefId);
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
        p.anyChildren(e, null, m::getMenuItems, m::setMenuItems, p.oneOf(N2oSimpleMenu.AbstractMenuItem.class)
                .add("menu-item", N2oSimpleMenu.MenuItem.class, this::menuItem)
                .add("dropdown-menu", N2oSimpleMenu.DropdownMenuItem.class, this::dropDownMenu));
    }

    private void menuItem(Element e, N2oSimpleMenu.MenuItem m, IOProcessor p) {
        p.attribute(e, "id", m::getId, m::setId);
        p.attribute(e, "name", m::getName, m::setName);
        p.attribute(e, "datasource", m::getDatasourceId, m::setDatasourceId);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attributeEnum(e, "icon-position", m::getIconPosition, m::setIconPosition, Position.class);
        p.attribute(e, "image", m::getImage, m::setImage);
        p.attributeEnum(e, "image-shape", m::getImageShape, m::setImageShape, ShapeType.class);
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.attribute(e, "class", m::getCssClass, m::setCssClass);
        p.attribute(e, "style", m::getStyle, m::setStyle);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
        p.anyChild(e, null, m::getAction, m::setAction, p.anyOf(N2oAction.class), ActionIOv2.NAMESPACE);
        badge(e, m, p);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
    }

    private void dropDownMenu(Element e, N2oSimpleMenu.DropdownMenuItem m, IOProcessor p) {
        p.attribute(e, "id", m::getId, m::setId);
        p.attribute(e, "name", m::getName, m::setName);
        p.attribute(e, "datasource", m::getDatasourceId, m::setDatasourceId);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attributeEnum(e, "icon-position", m::getIconPosition, m::setIconPosition, Position.class);
        p.attribute(e, "image", m::getImage, m::setImage);
        p.attributeEnum(e, "image-shape", m::getImageShape, m::setImageShape, ShapeType.class);
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
        p.anyChildren(e, null, m::getMenuItems, m::setMenuItems, p.oneOf(N2oSimpleMenu.AbstractMenuItem.class)
                .add("menu-item", N2oSimpleMenu.MenuItem.class, this::menuItem)
                .add("dropdown-menu", N2oSimpleMenu.DropdownMenuItem.class, this::dropDownMenu));
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
    }
}
