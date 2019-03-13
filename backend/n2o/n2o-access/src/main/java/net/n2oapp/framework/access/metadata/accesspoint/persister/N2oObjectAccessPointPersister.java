package net.n2oapp.framework.access.metadata.accesspoint.persister;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectAccessPoint;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

@Component
public class N2oObjectAccessPointPersister extends N2oAccessPointPersister<N2oObjectAccessPoint> {

    @Override
    public Element persist(N2oObjectAccessPoint accessPoint, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element rootElement = new Element(getElementName(), namespace);
        setAttribute(rootElement, "object-id", accessPoint.getObjectId());
        if (accessPoint.getAction() != null) {
            setAttribute(rootElement, "actions", accessPoint.getAction());
        }
        return rootElement;
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
