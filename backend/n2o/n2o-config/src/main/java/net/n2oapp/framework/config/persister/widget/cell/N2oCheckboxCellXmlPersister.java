package net.n2oapp.framework.config.persister.widget.cell;

import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCheckboxCell;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;
import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setEmptyElement;

/**
 * @author rgalina
 * @since 03.03.2016
 */
@Component
public class N2oCheckboxCellXmlPersister extends N2oCellXmlPersister<N2oCheckboxCell> {
    @Override
    public Element persist(N2oCheckboxCell checkbox, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        Element invokeAction = new Element("invoke-action", namespace);
        if (checkbox.getAction() != null) {
            root.setAttribute("action-id", ((N2oInvokeAction)checkbox.getAction()).getOperationId());
        };
        Element dependencies = setEmptyElement(root, "dependencies");
        if (checkbox.getEnablingCondition() != null) {
            Element enablingCondition = setEmptyElement(dependencies, "enabling-condition");
            setAttribute(enablingCondition, "on", checkbox.getEnablingCondition().getOn());
        }
        root.addContent(invokeAction);
        return root;
    }

    @Override
    public Class<N2oCheckboxCell> getElementClass() {
        return N2oCheckboxCell.class;
    }

    @Override
    public String getElementName() {
        return "checkbox";
    }
}
