package net.n2oapp.framework.access.metadata.accesspoint.persister;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oReferenceAccessPoint;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

@Component
public class N2oReferenceAccessPointPersister extends N2oAccessPointPersister<N2oReferenceAccessPoint> {
    @Override
    public Element persist(N2oReferenceAccessPoint reference, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element rootElement = new Element(getElementName(), namespace);
        setAttribute(rootElement, "object-id", reference.getObjectId());
        return rootElement;
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
