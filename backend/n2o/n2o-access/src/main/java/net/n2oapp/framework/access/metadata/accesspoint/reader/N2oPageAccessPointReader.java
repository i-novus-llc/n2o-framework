package net.n2oapp.framework.access.metadata.accesspoint.reader;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oPageAccessPoint;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class N2oPageAccessPointReader extends AbstractN2oAccessPointReader<N2oPageAccessPoint> {
    @Override
    public N2oPageAccessPoint read(Element element) {
        N2oPageAccessPoint res = new N2oPageAccessPoint();
        res.setPage(ReaderJdomUtil.getAttributeString(element, "pages"));
        res.setNamespaceUri(getNamespaceUri());
        return res;
    }

    @Override
    public String getElementName() {
        return "page-access";
    }

    @Override
    public Class<N2oPageAccessPoint> getElementClass() {
        return N2oPageAccessPoint.class;
    }

}
