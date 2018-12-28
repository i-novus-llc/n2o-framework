package net.n2oapp.framework.config.persister.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oIconCell;
import net.n2oapp.framework.config.persister.widget.SwitchPersister;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * @author rgalina
 * @since 03.03.2016
 */
@Component
public class N2oIconCellXmlPersister extends N2oCellXmlPersister<N2oIconCell> {

    @Override
    public Element persist(N2oIconCell icon, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        setAttribute(root, "icon-field-id", icon.getIconSwitch().getFieldId());
        Element iconSwitch = SwitchPersister.persist(icon.getIconSwitch(), namespace);
        if (iconSwitch  != null) {
            root.addContent(iconSwitch);
        }
        setAttribute(root, "type", icon.getType());
        setAttribute(root, "position", icon.getPosition());
        return root;
    }

    @Override
    public Class<N2oIconCell> getElementClass() {
        return N2oIconCell.class;
    }

    @Override
    public String getElementName() {
        return "icon";
    }
}
