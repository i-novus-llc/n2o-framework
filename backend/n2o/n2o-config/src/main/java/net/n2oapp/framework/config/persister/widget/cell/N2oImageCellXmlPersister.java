package net.n2oapp.framework.config.persister.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageCell;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * @author rgalina
 * @since 03.03.2016
 */
@Component
public class N2oImageCellXmlPersister extends N2oCellXmlPersister<N2oImageCell> {

    @Override
    public Element persist(N2oImageCell image, Namespace namespaceElemnt) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        setAttribute(root, "width", image.getWidth());
        setAttribute(root, "shape", image.getShape());
        return root;
    }

    @Override
    public Class<N2oImageCell> getElementClass() {
        return N2oImageCell.class;
    }

    @Override
    public String getElementName() {
        return "image";
    }
}
