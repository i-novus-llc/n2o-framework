package net.n2oapp.framework.config.io.application;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.application.NavigationLayoutEnum;
import net.n2oapp.framework.api.metadata.event.N2oAbstractEvent;
import net.n2oapp.framework.api.metadata.event.N2oStompEvent;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.application.header.HeaderIOv2;
import net.n2oapp.framework.config.io.application.sidebar.SidebarIOv2;
import net.n2oapp.framework.config.io.datasource.DatasourceIOv1;
import net.n2oapp.framework.config.io.event.StompEventIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Запись/чтение приложения версии 2.0
 */
@Component
public class ApplicationIOv2 implements NamespaceIO<N2oApplication> {

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
        return "http://n2oapp.net/framework/config/schema/application-2.0";
    }

    @Override
    public void io(Element e, N2oApplication m, IOProcessor p) {
        p.attributeEnum(e, "navigation-layout", m::getNavigationLayout, m::setNavigationLayout, NavigationLayoutEnum.class);
        p.attribute(e, "welcome-page-id", m::getWelcomePageId, m::setWelcomePageId);
        p.attributeBoolean(e, "navigation-layout-fixed", m::getNavigationLayoutFixed, m::setNavigationLayoutFixed);
        p.child(e, null, "header", m::getHeader, m::setHeader, new HeaderIOv2());
        p.children(e, null, "sidebar", m::getSidebars, m::setSidebars, new SidebarIOv2());
        p.child(e, null, "footer", m::getFooter, m::setFooter, new FooterIO());
        p.anyChildren(e, "datasources", m::getDatasources, m::setDatasources, p.anyOf(N2oAbstractDatasource.class), DatasourceIOv1.NAMESPACE);
        p.anyChildren(e, "events", m::getEvents, m::setEvents, p.oneOf(N2oAbstractEvent.class)
                .add("stomp-event", N2oStompEvent.class, new StompEventIO()));
    }
}
