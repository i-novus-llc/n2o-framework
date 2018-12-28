package net.n2oapp.framework.access.metadata.accesspoint.persister;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oFilterAccessPoint;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

@Component
public class N2oFilterAccessPointPersister extends N2oAccessPointPersister<N2oFilterAccessPoint> {
    @Override
    public Class<N2oFilterAccessPoint> getElementClass() {
        return N2oFilterAccessPoint.class;
    }

    @Override
    public Element persist(N2oFilterAccessPoint entity, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element rootElement = new Element(getElementName(), namespace);
        setAttribute(rootElement, "query-id", entity.getQueryId());
        setAttribute(rootElement, "filters", entity.getFilterId());
        return rootElement;
    }

    @Override
    public String getElementName() {
        return "filter-access";
    }
}
