package net.n2oapp.framework.access.metadata.accesspoint.reader;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oFilterAccessPoint;
import org.jdom.Element;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;

@Component
public class N2oFilterAccessPointReader extends AbstractN2oAccessPointReader<N2oFilterAccessPoint> {
    @Override
    public String getElementName() {
        return "filter-access";
    }

    @Override
    public N2oFilterAccessPoint read(Element element) {
        N2oFilterAccessPoint point = new N2oFilterAccessPoint();
        point.setQueryId(getAttributeString(element, "query-id"));
        point.setFilterId(getAttributeString(element, "filters"));
        point.setNamespaceUri(getNamespaceUri());
        return point;
    }

    @Override
    public Class<N2oFilterAccessPoint> getElementClass() {
        return N2oFilterAccessPoint.class;
    }
}
