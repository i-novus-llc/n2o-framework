package net.n2oapp.framework.config.metadata.compile.application;

import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.metadata.compile.header.HeaderIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Запись/чтение приложения
 */
@Component
public class ApplicationIO implements NamespaceIO<N2oApplication> {
    @Override
    public Class<N2oApplication> getElementClass() {
        return N2oApplication.class;
    }

    @Override
    public String getElementName() {
        return "application";
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/application-1.0";
    }

    @Override
    public void io(Element e, N2oApplication m, IOProcessor p) {
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.attribute(e, "navigation-layout", m::getNavigationLayout, m::setNavigationLayout);
        p.attribute(e, "welcome-page-id", m::getWelcomePageId, m::setWelcomePageId);
        p.attributeBoolean(e, "navigation-layout-fixed", m::getNavigationLayoutFixed, m::setNavigationLayoutFixed);
        p.child(e, null, "header", m::getHeader, m::setHeader, new HeaderIO());
        p.child(e, null, "sidebar", m::getSidebar, m::setSidebar, new SidebarIO());
        p.child(e, null, "footer", m::getFooter, m::setFooter, new FooterIO());
    }
}
