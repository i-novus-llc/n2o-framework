package net.n2oapp.framework.config.persister.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oColorCell;
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
public class N2oColorCellXmlPersister extends N2oCellXmlPersister<N2oColorCell> {
    @Override
    public Element persist(N2oColorCell color, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        setAttribute(root, "color-field-id", color.getStyleSwitch().getFieldId());
        Element styleSwitch = SwitchPersister.persist(color.getStyleSwitch(), namespace);
        if (styleSwitch != null) {
            root.addContent(styleSwitch);
        }
        return root;
    }

    @Override
    public Class<N2oColorCell> getElementClass() {
        return N2oColorCell.class;
    }

    @Override
    public String getElementName() {
        return "color";
    }
}
