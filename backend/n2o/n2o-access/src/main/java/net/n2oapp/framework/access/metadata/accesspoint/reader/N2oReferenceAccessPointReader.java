package net.n2oapp.framework.access.metadata.accesspoint.reader;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oReferenceAccessPoint;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class N2oReferenceAccessPointReader extends AbstractN2oAccessPointReader<N2oReferenceAccessPoint> {

    @Override
    public N2oReferenceAccessPoint read(Element element) {
        N2oReferenceAccessPoint res = new N2oReferenceAccessPoint();
        res.setObjectId(ReaderJdomUtil.getAttributeString(element, "object-id"));
        res.setNamespaceUri(getNamespaceUri());
        return res;
    }

    @Override
    public Class<N2oReferenceAccessPoint> getElementClass() {
        return N2oReferenceAccessPoint.class;
    }

    @Override
    public String getElementName() {
        return "reference-access";
    }
}
