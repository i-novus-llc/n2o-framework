package net.n2oapp.framework.config.metadata.compile.application;

import net.n2oapp.framework.api.metadata.application.N2oSidebar;
import net.n2oapp.framework.api.metadata.application.Side;
import net.n2oapp.framework.api.metadata.application.SidebarState;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.config.io.control.v2.ComponentIO;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuIOv3;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись боковой панели версии 2.0
 */
@Component
public class SidebarIOv2 extends ComponentIO<N2oSidebar> {
    @Override
    public String getElementName() {
        return "sidebar";
    }

    @Override
    public Class<N2oSidebar> getElementClass() {
        return N2oSidebar.class;
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/application-2.0";
    }

    @Override
    public void io(Element e, N2oSidebar m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeBoolean(e, "visible", m::getVisible, m::setVisible);
        p.attributeEnum(e, "side", m::getSide, m::setSide, Side.class);
        p.attribute(e, "logo-src", m::getLogoSrc, m::setLogoSrc);
        p.attribute(e, "title", m::getTitle, m::setTitle);
        p.attribute(e, "home-page-url", m::getHomePageUrl, m::setHomePageUrl);
        p.attribute(e, "logo-class", m::getLogoClass, m::setLogoClass);
        p.attributeEnum(e, "default-state", m::getDefaultState, m::setDefaultState, SidebarState.class);
        p.attributeEnum(e, "toggled-state", m::getToggledState, m::setToggledState, SidebarState.class);
        p.attributeBoolean(e, "toggle-on-hover", m::getToggleOnHover, m::setToggleOnHover);
        p.attributeBoolean(e, "overlay", m::getOverlay, m::setOverlay);
        p.child(e, null, "nav", m::getMenu, m::setMenu, N2oSimpleMenu.class, new SimpleMenuIOv3());
        p.child(e, null, "extra-menu", m::getExtraMenu, m::setExtraMenu, N2oSimpleMenu.class, new SimpleMenuIOv3());
    }
}
