package net.n2oapp.framework.access.metadata.accesspoint.persister;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oModuleAccessPoint;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

@Component
public class N2oModuleAccessPointPersister extends N2oAccessPointPersister<N2oModuleAccessPoint> {
    @Override
    public Element persist(N2oModuleAccessPoint menuItem, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element rootElement = new Element(getElementName(), namespace);
        setAttribute(rootElement, "modules", menuItem.getModule());
        return rootElement;
    }

    @Override
    public Class<N2oModuleAccessPoint> getElementClass() {
        return N2oModuleAccessPoint.class;
    }

    @Override
    public String getElementName() {
        return "module-access";
    }
}
