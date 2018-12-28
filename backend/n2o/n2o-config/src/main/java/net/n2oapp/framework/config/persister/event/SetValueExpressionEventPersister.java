package net.n2oapp.framework.config.persister.event;

import net.n2oapp.framework.api.metadata.event.action.SetValueExpressionAction;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;


/**
 * Сохраняет событие set-value-expression в xml-файл
 */
@Component
public class SetValueExpressionEventPersister extends N2oEventXmlPersister<SetValueExpressionAction> {

    private static final SetValueExpressionEventPersister instance = new SetValueExpressionEventPersister();

    public SetValueExpressionEventPersister() {

    }

    public static SetValueExpressionEventPersister getInstance() {
        return instance;
    }

    @Override
    public Element persist(SetValueExpressionAction event, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        root.setText(event.getExpression());
        return root;
    }

    @Override
    public Class<SetValueExpressionAction> getElementClass() {
        return SetValueExpressionAction.class;
    }

    @Override
    public String getElementName() {
        return "set-value-expression";
    }
}
