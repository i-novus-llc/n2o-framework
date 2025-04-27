package net.n2oapp.framework.config.io.toolbar.v2;

import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oSubmenu;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.tablesettings.TableSettingsIOv1;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись кнопки с выпадающим меню версии 2.0
 */
@Component
public class SubmenuIOv2 extends AbstractButtonIOv2<N2oSubmenu> {

    protected Namespace defaultNamespace = AbstractButtonIOv2.NAMESPACE;

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
        p.anyChildren(e, null, s::getMenuItems, s::setMenuItems, p.anyOf(N2oButton.class)
                        .add("menu-item", defaultNamespace.getURI(), N2oButton.class, new ButtonIOv2()),
                defaultNamespace, TableSettingsIOv1.NAMESPACE);
    }

    @Override
    public String getNamespaceUri() {
        return defaultNamespace.getURI();
    }
}
