package net.n2oapp.framework.config.io.toolbar;

import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oMenuItem;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oSubmenu;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение и запись sub-menu
 */
@Component
public class SubmenuIO implements NamespaceIO<N2oSubmenu> {

    protected Namespace defaultNamespace = AbstractMenuItemIO.NAMESPACE;

    @Override
    public Class<N2oSubmenu> getElementClass() {
        return N2oSubmenu.class;
    }

    @Override
    public String getElementName() {
        return "sub-menu";
    }

    @Override
    public void io(Element e, N2oSubmenu s, IOProcessor p) {
        p.attribute(e, "id", s::getId, s::setId);
        p.attribute(e, "src", s::getSrc, s::setSrc);
        p.attribute(e, "label", s::getLabel, s::setLabel);
        p.attribute(e, "color", s::getColor, s::setColor);
        p.attributeBoolean(e, "visible", s::getVisible, s::setVisible);
        p.attribute(e, "description", s::getDescription, s::setDescription);
        p.attribute(e, "icon", s::getIcon, s::setIcon);
        p.attribute(e, "class", s::getClassName, s::setClassName);
        p.attribute(e, "style", s::getStyle, s::setStyle);
        p.attributeEnum(e, "type", s::getType, s::setType, LabelType.class);
        p.attributeArray(e, "generate", ",", s::getGenerate, s::setGenerate);
        p.children(e, null, "menu-item", s::getMenuItems, s::setMenuItems, N2oMenuItem.class, new MenuItemIO());
    }

    @Override
    public String getNamespaceUri() {
        return defaultNamespace.getURI();
    }
}
