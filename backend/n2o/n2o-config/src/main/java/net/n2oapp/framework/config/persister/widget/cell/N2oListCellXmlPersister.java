package net.n2oapp.framework.config.persister.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oListCell;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import net.n2oapp.framework.config.persister.widget.SwitchPersister;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * @author V. Alexeev.
 */
@Component
public class N2oListCellXmlPersister extends N2oCellXmlPersister<N2oListCell> {
    @Override
    public Class<N2oListCell> getElementClass() {
        return N2oListCell.class;
    }

    @Override
    public Element persist(N2oListCell entity, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        PersisterJdomUtil.setAttribute(root, "label-field-id", entity.getLabelFieldId());
        Element badgeSwitch = SwitchPersister.persist(entity.getN2oSwitch(), namespace);
        if (badgeSwitch != null) {
            root.addContent(badgeSwitch);
        }
        return root;
    }

    @Override
    public String getElementName() {
        return "list";
    }
}
