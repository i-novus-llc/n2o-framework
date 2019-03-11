package net.n2oapp.framework.access.metadata.accesspoint.io;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oPageAccessPoint;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Реализация IO для доступа к странице 'page-access' v2.0
 * net/n2oapp/framework/config/schema/access-point-2.0.xsd
 */

@Component
public class PageAccessPointIOv2 extends AccessPointElementIOv2<N2oPageAccessPoint> {
    @Override
    public Class<N2oPageAccessPoint> getElementClass() {
        return N2oPageAccessPoint.class;
    }

    @Override
    public String getElementName() {
        return "page-access";
    }

    @Override
    public void io(Element e, N2oPageAccessPoint m, IOProcessor p) {
        p.attribute(e, "page-id", m::getPage, m::setPage);
    }
}
