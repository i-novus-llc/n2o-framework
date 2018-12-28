package net.n2oapp.framework.access.metadata.accesspoint.reader;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oMenuItemAccessPoint;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class N2oMenuAccessPointReader extends AbstractN2oAccessPointReader<N2oMenuItemAccessPoint> {
    @Override
    public N2oMenuItemAccessPoint read(Element element) {
        N2oMenuItemAccessPoint res = new N2oMenuItemAccessPoint();
        res.setContainer(ReaderJdomUtil.getAttributeString(element, "container"));
        res.setPage(ReaderJdomUtil.getAttributeString(element, "page"));
        res.setMenuItem(ReaderJdomUtil.getAttributeString(element, "menu-items"));
        res.setNamespaceUri(getNamespaceUri());
        return res;
    }

    @Override
    public String getElementName() {
        return "menu-access";
    }

    @Override
    public Class<N2oMenuItemAccessPoint> getElementClass() {
        return N2oMenuItemAccessPoint.class;
    }

}
