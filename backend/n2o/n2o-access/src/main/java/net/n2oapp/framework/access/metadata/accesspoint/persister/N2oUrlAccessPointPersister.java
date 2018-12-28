package net.n2oapp.framework.access.metadata.accesspoint.persister;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oUrlAccessPoint;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

@Component
public class N2oUrlAccessPointPersister extends N2oAccessPointPersister<N2oUrlAccessPoint> {

    @Override
    public Element persist(N2oUrlAccessPoint url, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element rootElement = new Element(getElementName(), namespace);
        setAttribute(rootElement, "pattern", url.getPattern());
        return rootElement;
    }

    @Override
    public Class<N2oUrlAccessPoint> getElementClass() {
        return N2oUrlAccessPoint.class;
    }

    @Override
    public String getElementName() {
        return "url-access";
    }
}
