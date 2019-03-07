package net.n2oapp.framework.access.metadata.accesspoint.io;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectAccessPoint;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Реализация IO для доступа к объекту 'object-access' v2.0
 * net/n2oapp/framework/config/schema/access-point-2.0.xsd
 */

@Component
public class ObjectAccessPointIOv2 extends AccessPointElementIOv2<N2oObjectAccessPoint> {

    @Override
    public void io(Element e, N2oObjectAccessPoint t, IOProcessor p) {
        p.attribute(e, "object-id", t::getObjectId, t::setObjectId);
        p.attribute(e, "operations", t::getAction, t::setAction);
        p.attributeArray(e, "remove-filters", ",", t::getRemoveFilters, t::setRemoveFilters);
    }

    @Override
    public Class<N2oObjectAccessPoint> getElementClass() {
        return N2oObjectAccessPoint.class;
    }

    @Override
    public String getElementName() {
        return "object-access";
    }
}
