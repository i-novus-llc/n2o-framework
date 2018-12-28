package net.n2oapp.framework.access.metadata.accesspoint.reader;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oColumnAccessPoint;
import org.jdom.Element;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;

@Component
public class N2oColumnAccessPointReader extends AbstractN2oAccessPointReader<N2oColumnAccessPoint> {
    @Override
    public String getElementName() {
        return "column-access";
    }

    @Override
    public N2oColumnAccessPoint read(Element element) {
        N2oColumnAccessPoint point = new N2oColumnAccessPoint();
        point.setPageId(getAttributeString(element, "page-id"));
        point.setContainerId(getAttributeString(element, "container-id"));
        point.setColumnId(getAttributeString(element, "columns"));
        point.setNamespaceUri(getNamespaceUri());
        return point;
    }

    @Override
    public Class<N2oColumnAccessPoint> getElementClass() {
        return N2oColumnAccessPoint.class;
    }
}
