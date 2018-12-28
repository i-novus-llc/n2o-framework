package net.n2oapp.framework.access.metadata.accesspoint.persister;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oColumnAccessPoint;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

@Component
public class N2oColumnAccessPointPersister extends N2oAccessPointPersister<N2oColumnAccessPoint> {
    @Override
    public Class<N2oColumnAccessPoint> getElementClass() {
        return N2oColumnAccessPoint.class;
    }

    @Override
    public Element persist(N2oColumnAccessPoint entity, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element rootElement = new Element(getElementName(), namespace);
        setAttribute(rootElement, "page-id", entity.getPageId());
        setAttribute(rootElement, "container-id", entity.getContainerId());
        setAttribute(rootElement, "columns", entity.getPageId());
        return rootElement;
    }

    @Override
    public String getElementName() {
        return "column-access";
    }
}
