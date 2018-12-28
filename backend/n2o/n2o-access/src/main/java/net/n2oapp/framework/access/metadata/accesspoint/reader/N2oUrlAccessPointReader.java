package net.n2oapp.framework.access.metadata.accesspoint.reader;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oUrlAccessPoint;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class N2oUrlAccessPointReader extends AbstractN2oAccessPointReader<N2oUrlAccessPoint> {
    @Override
    public N2oUrlAccessPoint read(Element element) {
        N2oUrlAccessPoint res = new N2oUrlAccessPoint();
        res.setPattern(ReaderJdomUtil.getAttributeString(element, "pattern"));
        res.setNamespaceUri(getNamespaceUri());
        return res;
    }

    @Override
    public Class<N2oUrlAccessPoint> getElementClass() {
        return N2oUrlAccessPoint.class;
    }

    @Override
    public String getElementName() {
        return "url-access";
    }
}
