package net.n2oapp.framework.access.metadata.accesspoint.persister;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oMenuItemAccessPoint;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

@Component
public class N2oMenuAccessPointPersister extends N2oAccessPointPersister<N2oMenuItemAccessPoint> {
    @Override
    public Element persist(N2oMenuItemAccessPoint menuItem, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element rootElement = new Element(getElementName(), namespace);
        setAttribute(rootElement, "page", menuItem.getPage());
        setAttribute(rootElement, "container", menuItem.getContainer());
        setAttribute(rootElement, "menu-items", menuItem.getMenuItem());
        return rootElement;
    }

    @Override
    public Class<N2oMenuItemAccessPoint> getElementClass() {
        return N2oMenuItemAccessPoint.class;
    }

    @Override
    public String getElementName() {
        return "menu-access";
    }
}
