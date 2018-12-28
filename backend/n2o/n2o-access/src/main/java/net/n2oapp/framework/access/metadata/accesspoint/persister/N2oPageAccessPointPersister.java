package net.n2oapp.framework.access.metadata.accesspoint.persister;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oPageAccessPoint;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

@Component
public class N2oPageAccessPointPersister extends N2oAccessPointPersister<N2oPageAccessPoint> {
    @Override
    public Element persist(N2oPageAccessPoint page, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element rootElement = new Element(getElementName(), namespace);
        setAttribute(rootElement, "pages", page.getPage());
        return rootElement;
    }

    @Override
    public Class<N2oPageAccessPoint> getElementClass() {
        return N2oPageAccessPoint.class;
    }

    @Override
    public String getElementName() {
        return "page-access";
    }
}
