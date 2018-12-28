package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.list.N2oCheckboxButtons;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * @author dfirstov
 * @since 06.02.2015
 */
@Component
public class N2oCheckboxButtonsPersister extends N2oControlXmlPersister<N2oCheckboxButtons> {
    @Override
    public Element persist(N2oCheckboxButtons control, Namespace namespace) {
        Element element = new Element(getElementName(), namespacePrefix, namespaceUri);
        PersisterJdomUtil.setAttribute(element, "color-field-id", control.getColorFieldId());
        element.setAttribute("data-toggle", "buttons-checkbox");
        setControl(element, control);
        setField(element, control);
        setListField(element, control);
        setListQuery(element, control);
        setOptionsList(element, control.getOptions());
        return element;
    }

    @Override
    public Class<N2oCheckboxButtons> getElementClass() {
        return N2oCheckboxButtons.class;
    }

    @Override
    public String getElementName() {
        return "buttons";
    }
}
