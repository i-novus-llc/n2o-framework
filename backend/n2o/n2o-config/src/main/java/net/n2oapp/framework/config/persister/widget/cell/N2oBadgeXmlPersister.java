package net.n2oapp.framework.config.persister.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oBadgeCell;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import net.n2oapp.framework.config.persister.widget.SwitchPersister;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * @author V. Alexeev.
 * @date 18.03.2016
 */
@Component
public class N2oBadgeXmlPersister extends N2oCellXmlPersister<N2oBadgeCell> {

    @Override
    public Element persist(N2oBadgeCell entity, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        PersisterJdomUtil.setAttribute(root, "text", entity.getText());
        PersisterJdomUtil.setAttribute(root, "position", entity.getPosition());
        Element badgeSwitch = SwitchPersister.persist(entity.getN2oSwitch(), namespace);
        if(badgeSwitch != null) root.addContent(badgeSwitch);
        return root;
    }

    @Override
    public Class<N2oBadgeCell> getElementClass() {
        return N2oBadgeCell.class;
    }

    @Override
    public String getElementName() {
        return "badge";
    }
}
