package net.n2oapp.framework.config.persister.event;

import net.n2oapp.framework.api.metadata.event.action.N2oActionAndClose;
import org.jdom.Element;
import org.jdom.Namespace;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * Persister для события <invoke-action-and-close>
 */
public class ActionAndClosePersister extends N2oEventXmlPersister<N2oActionAndClose> {
    private static final ActionAndClosePersister instance = new ActionAndClosePersister();

    public static ActionAndClosePersister getInstance() {
        return instance;
    }

    private ActionAndClosePersister() {
    }

    @Override
    public Element persist(N2oActionAndClose actionAndClose, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        setAttribute(root, "action-id", actionAndClose.getOperationId());
        setAttribute(root, "confirmation", actionAndClose.getConfirmation());
        return root;
    }

    @Override
    public Class<N2oActionAndClose> getElementClass() {
        return N2oActionAndClose.class;
    }

    @Override
    public String getElementName() {
        return "invoke-action-and-close";
    }
}
