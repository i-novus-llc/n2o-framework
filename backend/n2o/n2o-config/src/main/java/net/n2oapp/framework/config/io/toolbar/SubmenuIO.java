package net.n2oapp.framework.config.io.toolbar;

import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oSubmenu;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись кнопки с выпадающим меню
 */
@Component
public class SubmenuIO extends AbstractButtonIO<N2oSubmenu> {

    protected Namespace defaultNamespace = AbstractButtonIO.NAMESPACE;

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
        super.io(e, s, p);
        p.attributeArray(e, "generate", ",", s::getGenerate, s::setGenerate);
        p.attributeBoolean(e, "show-toggle-icon", s::getShowToggleIcon, s::setShowToggleIcon);
        p.attribute(e, "visible", s::getVisible, s::setVisible);
        p.children(e, null, "menu-item", s::getMenuItems, s::setMenuItems, N2oButton.class, new ButtonIO());
    }

    @Override
    public String getNamespaceUri() {
        return defaultNamespace.getURI();
    }
}
