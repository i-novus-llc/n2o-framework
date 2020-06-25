package net.n2oapp.framework.config.persister.event;

import net.n2oapp.framework.api.metadata.event.action.N2oSetValueAction;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;


/**
 * Сохраняет событие set-value-expression в xml-файл
 */
@Component
public class SetValueExpressionEventPersister extends N2oEventXmlPersister<N2oSetValueAction> {

    private static final SetValueExpressionEventPersister instance = new SetValueExpressionEventPersister();

    public SetValueExpressionEventPersister() {

    }

    public static SetValueExpressionEventPersister getInstance() {
        return instance;
    }

    @Override
    public Element persist(N2oSetValueAction event, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        root.setText(event.getExpression());
        return root;
    }

    @Override
    public Class<N2oSetValueAction> getElementClass() {
        return N2oSetValueAction.class;
    }

    @Override
    public String getElementName() {
        return "set-value-expression";
    }
}
