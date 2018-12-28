package net.n2oapp.framework.access.metadata.accesspoint.reader;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oContainerAccessPoint;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class N2oContainerAccessPointReader extends AbstractN2oAccessPointReader<N2oContainerAccessPoint> {
    @Override
    public N2oContainerAccessPoint read(Element element) {
        N2oContainerAccessPoint res = new N2oContainerAccessPoint();
        res.setContainer(ReaderJdomUtil.getAttributeString(element, "containers"));
        res.setPage(ReaderJdomUtil.getAttributeString(element, "page"));
        res.setNamespaceUri(getNamespaceUri());
        return res;
    }

    @Override
    public String getElementName() {
        return "container-access";
    }

    @Override
    public Class<N2oContainerAccessPoint> getElementClass() {
        return N2oContainerAccessPoint.class;
    }

}
