package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.multi.N2oCheckboxGrid;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;
import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setSubChild;

/**
 * Created by schirkova on 17.04.2015.
 */
@Component
public class N2oCheckboxGridPersister extends N2oControlXmlPersister<N2oCheckboxGrid> {
    @Override
    public Element persist(N2oCheckboxGrid control, Namespace namespace) {
        Element element = new Element(getElementName(), namespacePrefix, namespaceUri);
        setControl(element, control);
        setField(element, control);
        setListField(element, control);
        setListQuery(element, control);
        setOptionsList(element, control.getOptions());

        setSubChild(element, "columns", "column", control.getColumns(), (column, n) -> {
            Element itemElement = new Element("column", namespacePrefix, namespaceUri);
            setAttribute(itemElement, "name", column.getName());
            setAttribute(itemElement, "column-field-id", column.getColumnFieldId());
            return itemElement;
        });
        return element;
    }

    @Override
    public Class<N2oCheckboxGrid> getElementClass() {
        return N2oCheckboxGrid.class;
    }

    @Override
    public String getElementName() {
        return "checkbox-grid";
    }
}
