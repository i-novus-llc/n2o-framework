package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.multi.N2oEditGrid;
import net.n2oapp.framework.api.metadata.control.plain.N2oCodeEditor;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;
import org.jdom.Namespace;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;
import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setSubChild;

/**
 * @author V. Alexeev.
 */
@Component
public class N2oEditGridPersister extends N2oControlXmlPersister<N2oEditGrid> {

    @Override
    public Element persist(N2oEditGrid entity, Namespace namespace) {
        Element element = new Element(getElementName(), namespacePrefix, namespaceUri);
        PersisterJdomUtil.setElementString(element, "object-id", entity.getObjectId());

        setSubChild(element, "columns", "column", entity.getColumns(), (column,n) -> {
            Element itemElement = new Element("column", namespacePrefix, namespaceUri);
            setAttribute(itemElement, "name", column.getName());
            setAttribute(itemElement, "column-field-id", column.getColumnFieldId());
            return itemElement;
        });

        setControl(element, entity);
        setField(element, entity);
        return element;
    }

    @Override
    public Class<N2oEditGrid> getElementClass() {
        return N2oEditGrid.class;
    }

    @Override
    public String getElementName() {
        return "edit-grid";
    }
}
