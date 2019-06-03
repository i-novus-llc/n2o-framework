package net.n2oapp.framework.access.metadata.accesspoint.reader;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectAccessPoint;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;


@Component
public class N2oObjectAccessPointReader extends AbstractN2oAccessPointReader<N2oObjectAccessPoint> {
    @Override
    public N2oObjectAccessPoint read(Element element) {
        N2oObjectAccessPoint res = new N2oObjectAccessPoint();
        String actions = ReaderJdomUtil.getAttributeString(element, "actions");
        if (actions != null)
            res.setAction(actions);
        res.setObjectId(ReaderJdomUtil.getAttributeString(element, "object-id"));
        res.setNamespaceUri(getNamespaceUri());
        return res;
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
