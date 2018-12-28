package net.n2oapp.framework.config.persister.event;

import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * User: iryabov
 * Date: 04.07.13
 * Time: 17:37
 */
@Component
public class InvokeActionPersister extends N2oEventXmlPersister<N2oInvokeAction> {
    public static void setInvokeAction(N2oInvokeAction invokeEvent, Element root, Namespace namespace) {
        if (invokeEvent == null) return;
        Element invokeActionElement = new Element("invoke-action", namespace);
        persistInvokeAction(invokeEvent, invokeActionElement, namespace);
        root.addContent(invokeActionElement);
    }

    @Override
    public Element persist(N2oInvokeAction invokeEvent, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        persistInvokeAction(invokeEvent, root, namespace);
        return root;
    }

    private static void persistInvokeAction(N2oInvokeAction invokeEvent, Element invokeActionElement, Namespace namespace) {
        setAttribute(invokeActionElement, "action-id", invokeEvent.getOperationId());
    }

    @Override
    public Class<N2oInvokeAction> getElementClass() {
        return N2oInvokeAction.class;
    }

    @Override
    public String getElementName() {
        return "invoke-action";
    }
}
