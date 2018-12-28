package net.n2oapp.framework.config.persister.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oProgressBarCell;
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
public class N2oProgressBarCellXmlPersister extends N2oCellXmlPersister<N2oProgressBarCell> {

    @Override
    public Element persist(N2oProgressBarCell progressBar, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        Element styleSwitch = SwitchPersister.persist(progressBar.getStyleSwitch(), namespace);
        if (styleSwitch != null) {
            root.addContent(styleSwitch);
        }
        setAttribute(root, "size", progressBar.getSize());
        setAttribute(root, "striped", progressBar.getStriped());
        setAttribute(root, "active", progressBar.getActive());
        return root;
    }

    @Override
    public Class<N2oProgressBarCell> getElementClass() {
        return N2oProgressBarCell.class;
    }

    @Override
    public String getElementName() {
        return "progress-bar";
    }
}
