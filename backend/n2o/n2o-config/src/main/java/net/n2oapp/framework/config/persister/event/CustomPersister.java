package net.n2oapp.framework.config.persister.event;

import net.n2oapp.framework.api.metadata.event.action.N2oCustomAction;
import net.n2oapp.framework.config.persister.tools.PropertiesPersister;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

@Component
public class CustomPersister extends N2oEventXmlPersister<N2oCustomAction> {

    @Override
    public Element persist(N2oCustomAction custom, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        setAttribute(root, "src", custom.getSrc());
        if (custom.getProperties() != null && !custom.getProperties().isEmpty()) {
            PropertiesPersister propertiesPersister = new PropertiesPersister(namespace);
            root.addContent(propertiesPersister.persist(custom.getProperties(),namespace));
        }
        return root;
    }

    @Override
    public Class<N2oCustomAction> getElementClass() {
        return N2oCustomAction.class;
    }

    @Override
    public String getElementName() {
        return "custom";
    }
}
