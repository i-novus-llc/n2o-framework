package net.n2oapp.framework.config.metadata.compile.application;

import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.application.N2oDatasource;
import net.n2oapp.framework.api.metadata.application.N2oStompDatasource;
import net.n2oapp.framework.api.metadata.application.NavigationLayout;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.metadata.compile.header.HeaderIOv2;
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
        p.attributeEnum(e, "navigation-layout", m::getNavigationLayout, m::setNavigationLayout, NavigationLayout.class);
        p.attribute(e, "welcome-page-id", m::getWelcomePageId, m::setWelcomePageId);
        p.attributeBoolean(e, "navigation-layout-fixed", m::getNavigationLayoutFixed, m::setNavigationLayoutFixed);
        p.child(e, null, "header", m::getHeader, m::setHeader, new HeaderIOv2());
        p.child(e, null, "sidebar", m::getSidebar, m::setSidebar, new SidebarIOv2());
        p.child(e, null, "footer", m::getFooter, m::setFooter, new FooterIO());
        p.anyChildren(e, "datasources", m::getDatasources, m::setDatasources, p.oneOf(N2oDatasource.class)
                .add("datasource", N2oDatasource.class, this::datasource)
                .add("stomp-datasource", N2oStompDatasource.class, this::stompDatasource));
    }

    private  void datasource(Element e, N2oDatasource d, IOProcessor p) {
        p.attribute(e, "id", d::getId, d::setId);
    }

    private  void stompDatasource(Element e, N2oStompDatasource d, IOProcessor p) {
        datasource(e, d, p);
        p.attribute(e, "destination", d::getDestination, d::setDestination);
        //p.children(e, "values", );
    }
}
