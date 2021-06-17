package net.n2oapp.framework.config.metadata.compile.header;

import net.n2oapp.framework.api.metadata.header.N2oSearchBar;
import net.n2oapp.framework.api.metadata.header.N2oSimpleHeader;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuIOv2;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

@Component
public class SimpleHeaderIOv2 implements NamespaceIO<N2oSimpleHeader> {

    private Namespace menuDefaultNamespace = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/menu-2.0");

    @Override
    public Class<N2oSimpleHeader> getElementClass() {
        return N2oSimpleHeader.class;
    }

    @Override
    public String getElementName() {
        return "header";
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/header-2.0";
    }

    @Override
    public void io(Element e, N2oSimpleHeader m, IOProcessor p) {
        p.attribute(e, "home-page-url", m::getHomePageUrl, m::setHomePageUrl);
        p.attribute(e, "welcome-page-id", m::getWelcomePageId, m::setWelcomePageId);
        p.attribute(e, "brand-name", m::getTitle, m::setTitle);
        p.attribute(e, "logo-src", m::getLogoSrc, m::setLogoSrc);
        p.attribute(e, "class", m::getCssClass, m::setCssClass);
        p.attribute(e, "style", m::getStyle, m::setStyle);
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.child(e, null, "nav", m::getMenu, m::setMenu, N2oSimpleMenu.class, new SimpleMenuIOv2());
        p.child(e, null, "extra-menu", m::getExtraMenu, m::setExtraMenu, N2oSimpleMenu.class, new SimpleMenuIOv2());
        p.child(e, null, "search", m::getSearchBar, m::setSearchBar, N2oSearchBar.class, new N2oSearchBarIOv2());
    }
}
