package net.n2oapp.framework.config.persister.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oLink;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * Сохраняет link в xml-файл
 */
@Component
public class N2oLinkCellXmlPersister extends N2oCellXmlPersister<N2oLink> {
    @Override
    public Element persist(N2oLink link, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element element = new Element(getElementName(), namespace);
        Element eventElement = persisterFactory.produce(link.getAction()).persist(link.getAction(),namespace);
        PersisterJdomUtil.installPrefix(eventElement, element);
        element.addContent(eventElement);
        PersisterJdomUtil.setAttribute(element, "id", link.getId());
        return element;
    }

    @Override
    public Class<N2oLink> getElementClass() {
        return N2oLink.class;
    }

    @Override
    public String getElementName() {
        return "link";
    }
}
