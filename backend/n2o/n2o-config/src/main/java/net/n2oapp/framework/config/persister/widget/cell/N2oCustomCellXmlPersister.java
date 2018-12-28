package net.n2oapp.framework.config.persister.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCustomCell;
import net.n2oapp.framework.config.persister.tools.PropertiesPersister;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * @author rgalina
 * @since 03.03.2016
 */
@Component
public class N2oCustomCellXmlPersister extends N2oCellXmlPersister<N2oCustomCell> {
    @Override
    public Element persist(N2oCustomCell custom, Namespace namespaceElemnt) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element element = new Element(getElementName(), namespace);
        setAttribute(element, "src", custom.getSrc());
        element.addContent(new PropertiesPersister(namespace).persist(custom.getProperties(),namespace));
        return element;
    }

    @Override
    public Class<N2oCustomCell> getElementClass() {
        return N2oCustomCell.class;
    }

    @Override
    public String getElementName() {
        return "custom";
    }
}
