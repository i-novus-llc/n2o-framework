package net.n2oapp.framework.access.metadata.accesspoint.reader;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oModuleAccessPoint;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class N2oModuleAccessPointReader extends AbstractN2oAccessPointReader<N2oModuleAccessPoint> {
    @Override
    public N2oModuleAccessPoint read(Element element) {
        N2oModuleAccessPoint res = new N2oModuleAccessPoint();
        res.setModule(ReaderJdomUtil.getAttributeString(element, "modules"));
        res.setNamespaceUri(getNamespaceUri());
        return res;
    }

    @Override
    public String getElementName() {
        return "module-access";
    }

    @Override
    public Class<N2oModuleAccessPoint> getElementClass() {
        return N2oModuleAccessPoint.class;
    }

}
