package net.n2oapp.framework.access.metadata.accesspoint.io;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oUrlAccessPoint;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Реализация IO для доступа к URL 'url-access' v2.0
 * net/n2oapp/framework/config/schema/access-point-2.0.xsd
 */

@Component
public class UrlAccessPointIOv2 extends AccessPointElementIOv2<N2oUrlAccessPoint> {
    @Override
    public Class<N2oUrlAccessPoint> getElementClass() {
        return N2oUrlAccessPoint.class;
    }

    @Override
    public String getElementName() {
        return "url-access";
    }

    @Override
    public void io(Element e, N2oUrlAccessPoint m, IOProcessor p) {
        p.attribute(e, "pattern", m::getPattern, m::setPattern);
    }
}
