package net.n2oapp.framework.config.persister.event;

import net.n2oapp.framework.api.metadata.event.action.N2OValidateAction;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * Persister для события <validate>
 */
@Component
public class ValidateEventPersister extends N2oEventXmlPersister<N2OValidateAction> {
    private static final ValidateEventPersister instance = new ValidateEventPersister();

    public static ValidateEventPersister getInstance() {
        return instance;
    }

    private ValidateEventPersister() {
    }

    @Override
    public Element persist(N2OValidateAction validateEvent, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        return root;
    }

    @Override
    public Class<N2OValidateAction> getElementClass() {
        return N2OValidateAction.class;
    }

    @Override
    public String getElementName() {
        return "validate";
    }
}
