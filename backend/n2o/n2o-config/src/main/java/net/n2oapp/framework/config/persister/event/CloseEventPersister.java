package net.n2oapp.framework.config.persister.event;

import net.n2oapp.framework.api.metadata.event.action.N2oCloseAction;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Persister для события <close>
 */
public class CloseEventPersister extends N2oEventXmlPersister<N2oCloseAction> {
    private static final CloseEventPersister instance = new CloseEventPersister();

    public static CloseEventPersister getInstance() {
        return instance;
    }

    private CloseEventPersister() {
    }

    @Override
    public Element persist(N2oCloseAction closeEvent, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        return root;
    }

    @Override
    public Class<N2oCloseAction> getElementClass() {
        return N2oCloseAction.class;
    }

    @Override
    public String getElementName() {
        return "close";
    }
}
