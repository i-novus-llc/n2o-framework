package net.n2oapp.framework.config.io.application.sidebar;

import net.n2oapp.framework.api.metadata.application.N2oSidebar;
import net.n2oapp.framework.api.metadata.application.Side;
import net.n2oapp.framework.api.metadata.application.SidebarState;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.config.io.control.ComponentIO;
import net.n2oapp.framework.config.io.datasource.DatasourceIO;
import net.n2oapp.framework.config.io.menu.ExtraMenuIOv3;
import net.n2oapp.framework.config.io.menu.NavMenuIOv3;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись боковой панели версии 3.0
 */
@Component
public class SidebarIOv3 extends ComponentIO<N2oSidebar> {

    public static Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/sidebar-3.0");

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
        return NAMESPACE.getURI();
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
        p.attribute(e, "path", m::getPath, m::setPath);
        p.attribute(e, "subtitle", m::getSubtitle, m::setSubtitle);
        p.attribute(e, "datasource", m::getDatasourceId, m::setDatasourceId);
        p.attribute(e, "ref-id", m::getRefId, m::setRefId);
        p.attributeEnum(e, "default-state", m::getDefaultState, m::setDefaultState, SidebarState.class);
        p.attributeEnum(e, "toggled-state", m::getToggledState, m::setToggledState, SidebarState.class);
        p.attributeBoolean(e, "toggle-on-hover", m::getToggleOnHover, m::setToggleOnHover);
        p.attributeBoolean(e, "overlay", m::getOverlay, m::setOverlay);
        p.child(e, null, "nav", m::getMenu, m::setMenu, N2oSimpleMenu.class, new NavMenuIOv3());
        p.child(e, null, "extra-menu", m::getExtraMenu, m::setExtraMenu, N2oSimpleMenu.class, new ExtraMenuIOv3());
        p.child(e, null, "datasource", m::getDatasource, m::setDatasource, N2oDatasource.class, new DatasourceIO());
    }

}
