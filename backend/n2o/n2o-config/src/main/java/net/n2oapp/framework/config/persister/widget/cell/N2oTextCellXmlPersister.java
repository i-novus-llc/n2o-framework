package net.n2oapp.framework.config.persister.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
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
public class N2oTextCellXmlPersister extends N2oCellXmlPersister<N2oTextCell> {
    @Override
    public Element persist(N2oTextCell text, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        setAttribute(root, "style-field-id", text.getClassSwitch().getFieldId());
        Element styleSwitch = SwitchPersister.persist(text.getClassSwitch(), namespace);
        if (styleSwitch != null) {
            root.addContent(styleSwitch);
        }
        return root;
    }

    @Override
    public Class<N2oTextCell> getElementClass() {
        return N2oTextCell.class;
    }

    @Override
    public String getElementName() {
        return "text";
    }
}
