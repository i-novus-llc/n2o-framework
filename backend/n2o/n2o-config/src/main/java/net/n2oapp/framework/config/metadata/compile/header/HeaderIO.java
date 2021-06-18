package net.n2oapp.framework.config.metadata.compile.header;

import net.n2oapp.framework.api.metadata.header.N2oHeader;
import net.n2oapp.framework.api.metadata.header.N2oSearchBar;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.config.io.control.ComponentIO;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuIOv2;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Запись/чтение заголовка приложения
 */
@Component
public class HeaderIO extends ComponentIO<N2oHeader> {

    @Override
    public String getElementName() {
        return "header";
    }

    @Override
    public Class<N2oHeader> getElementClass() {
        return N2oHeader.class;
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/application-1.0";
    }

    @Override
    public void io(Element e, N2oHeader m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "home-page-url", m::getHomePageUrl, m::setHomePageUrl);
        p.attribute(e, "title", m::getTitle, m::setTitle);
        p.attribute(e, "logo-src", m::getLogoSrc, m::setLogoSrc);
        p.attribute(e, "sidebar-default-icon", m::getSidebarDefaultIcon, m::setSidebarDefaultIcon);
        p.attribute(e, "sidebar-toggled-icon", m::getSidebarToggledIcon, m::setSidebarToggledIcon);
        p.attributeBoolean(e, "visible", m::getVisible, m::setVisible);
        p.child(e, null, "nav", m::getMenu, m::setMenu, N2oSimpleMenu.class, new SimpleMenuIOv2());
        p.child(e, null, "extra-menu", m::getExtraMenu, m::setExtraMenu, N2oSimpleMenu.class, new SimpleMenuIOv2());
        p.child(e, null, "search", m::getSearchBar, m::setSearchBar, N2oSearchBar.class, new N2oSearchBarIOv2());
    }


}
