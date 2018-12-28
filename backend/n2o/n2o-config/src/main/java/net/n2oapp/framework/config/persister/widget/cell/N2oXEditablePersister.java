package net.n2oapp.framework.config.persister.widget.cell;

import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oXEditableCell;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * @author V. Alexeev.
 * @date 29.03.2016
 */
@Component
public class N2oXEditablePersister extends N2oCellXmlPersister<N2oXEditableCell> {



    @Override
    public Element persist(N2oXEditableCell entity, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        if (entity.getAction() != null) {
            root.setAttribute("action-id", ((N2oInvokeAction)entity.getAction()).getOperationId());
        }
        root.setAttribute("action-id", entity.getAction().getOperationId());
        root.setAttribute("format", entity.getFormat());
        Element field = persisterFactory.produce(entity.getN2oField()).persist(entity.getN2oField(), namespace);
        root.addContent(field);
        return root;
    }

    @Override
    public Class<N2oXEditableCell> getElementClass() {
        return N2oXEditableCell.class;
    }

    @Override
    public String getElementName() {
        return "x-editable";
    }
}
