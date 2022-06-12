package net.n2oapp.framework.config.io.application;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.application.*;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.application.header.HeaderIOv3;
import net.n2oapp.framework.config.io.datasource.DatasourceIO;
import net.n2oapp.framework.config.io.datasource.StompDatasourceIO;
import net.n2oapp.framework.config.io.event.StompEventIO;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 * Запись/чтение приложения версии 3.0
 */
@Component
public class ApplicationIOv3 implements NamespaceIO<N2oApplication> {

    private Namespace sidebarNamespace = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/sidebar-3.0");

    @Override
    public Class<N2oApplication> getElementClass() {
        return N2oApplication.class;
    }

    @Override
    public String getElementName() {
        return  "application";
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/application-3.0";
    }

    @Override
    public void io(Element e, N2oApplication m, IOProcessor p) {
        p.attributeEnum(e, "navigation-layout", m::getNavigationLayout, m::setNavigationLayout, NavigationLayout.class);
        p.attribute(e, "welcome-page-id", m::getWelcomePageId, m::setWelcomePageId);
        p.attributeBoolean(e, "navigation-layout-fixed", m::getNavigationLayoutFixed, m::setNavigationLayoutFixed);
        p.child(e, null, "header", m::getHeader, m::setHeader, new HeaderIOv3());
        p.child(e, null, "footer", m::getFooter, m::setFooter, new FooterIO());
        p.anyChildren(e, "datasources", m::getDatasources, m::setDatasources, p.oneOf(N2oAbstractDatasource.class)
                .add("datasource", N2oDatasource.class, new DatasourceIO())
                .add("stomp-datasource", N2oStompDatasource.class, new StompDatasourceIO()));
        p.anyChildren(e, "events", m::getEvents, m::setEvents, p.oneOf(N2oAbstractEvent.class)
                .add("stomp-event", N2oStompEvent.class, new StompEventIO()));
        p.anyChildren(e, "sidebars", m::getSidebars, m::setSidebars, p.anyOf(N2oSidebar.class), sidebarNamespace);
    }
}
