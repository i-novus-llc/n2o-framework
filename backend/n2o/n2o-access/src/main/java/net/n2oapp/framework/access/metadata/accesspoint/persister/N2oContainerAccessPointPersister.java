package net.n2oapp.framework.access.metadata.accesspoint.persister;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oContainerAccessPoint;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

@Component
public class N2oContainerAccessPointPersister extends N2oAccessPointPersister<N2oContainerAccessPoint> {
    @Override
    public Element persist(N2oContainerAccessPoint container, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element rootElement = new Element(getElementName(), namespace);
        PersisterJdomUtil.setAttribute(rootElement, "page", container.getPage());
        PersisterJdomUtil.setAttribute(rootElement, "containers", container.getContainer());
        return rootElement;
    }

    @Override
    public Class<N2oContainerAccessPoint> getElementClass() {
        return N2oContainerAccessPoint.class;
    }

    @Override
    public String getElementName() {
        return "container-access";
    }
}
