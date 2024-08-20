package net.n2oapp.framework.config.io.application.sidebar;

import net.n2oapp.framework.api.metadata.application.N2oSidebar;
import net.n2oapp.framework.api.metadata.application.SidebarSide;
import net.n2oapp.framework.api.metadata.application.SidebarState;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.config.io.datasource.StandardDatasourceIO;
import net.n2oapp.framework.config.io.menu.ExtraMenuIOv3;
import net.n2oapp.framework.config.io.menu.NavMenuIOv3;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись боковой панели версии 3.0
 */
@Component
public class SidebarIOv3 implements NamespaceIO<N2oSidebar> {

    public static final Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/sidebar-3.0");

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
        return "http://n2oapp.net/framework/config/schema/sidebar-3.0";
    }

    @Override
    public void io(Element e, N2oSidebar m, IOProcessor p) {
        p.attribute(e, "id", m::getId, m::setId);
        p.attribute(e, "ref-id", m::getRefId, m::setRefId);
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.attribute(e, "class", m::getCssClass, m::setCssClass);
        p.attribute(e, "style", m::getStyle, m::setStyle);
        p.attribute(e, "title", m::getTitle, m::setTitle);
        p.attribute(e, "subtitle", m::getSubtitle, m::setSubtitle);
        p.attributeEnum(e, "side", m::getSide, m::setSide, SidebarSide.class);
        p.attribute(e, "path", m::getPath, m::setPath);
        p.attribute(e, "datasource", m::getDatasourceId, m::setDatasourceId);
        p.attribute(e, "logo-src", m::getLogoSrc, m::setLogoSrc);
        p.attribute(e, "logo-class", m::getLogoClass, m::setLogoClass);
        p.attribute(e, "home-page-url", m::getHomePageUrl, m::setHomePageUrl);
        p.attributeEnum(e, "default-state", m::getDefaultState, m::setDefaultState, SidebarState.class);
        p.attributeEnum(e, "toggled-state", m::getToggledState, m::setToggledState, SidebarState.class);
        p.attributeBoolean(e, "toggle-on-hover", m::getToggleOnHover, m::setToggleOnHover);
        p.attributeBoolean(e, "overlay", m::getOverlay, m::setOverlay);

        p.child(e, null, "nav", m::getMenu, m::setMenu, N2oSimpleMenu.class, new NavMenuIOv3());
        p.child(e, null, "extra-menu", m::getExtraMenu, m::setExtraMenu, N2oSimpleMenu.class, new ExtraMenuIOv3());
        p.child(e, null, "datasource", m::getDatasource, m::setDatasource, N2oStandardDatasource.class, new StandardDatasourceIO());
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
    }
}
