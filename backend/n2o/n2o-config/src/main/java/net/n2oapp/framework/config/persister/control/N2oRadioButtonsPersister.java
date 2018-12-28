package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.list.N2oRadioButtons;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * @author dfirstov
 * @since 06.02.2015
 */
@Component
public class N2oRadioButtonsPersister extends N2oControlXmlPersister<N2oRadioButtons> {
    @Override
    public Element persist(N2oRadioButtons control, Namespace namespace) {
        Element element = new Element(getElementName(), namespacePrefix, namespaceUri);
        PersisterJdomUtil.setAttribute(element, "color-field-id", control.getColorFieldId());
        setControl(element, control);
        setField(element, control);
        setListField(element, control);
        setListQuery(element, control);
        element.setAttribute("data-toggle", "buttons-radio");
        setOptionsList(element, control.getOptions());
        return element;
    }

    @Override
    public Class<N2oRadioButtons> getElementClass() {
        return N2oRadioButtons.class;
    }

    @Override
    public String getElementName() {
        return "buttons";
    }
}
