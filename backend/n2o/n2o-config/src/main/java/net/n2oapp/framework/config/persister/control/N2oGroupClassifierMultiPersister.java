package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.list.N2oGroupClassifierMulti;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * @author dfirstov
 * @since 16.07.2015
 */
@Component
public class N2oGroupClassifierMultiPersister extends N2oControlXmlPersister<N2oGroupClassifierMulti> {
    @Override
    public Element persist(N2oGroupClassifierMulti control, Namespace namespace) {
        Element element = new Element(getElementName(), namespacePrefix, namespaceUri);
        setAttribute(element, "group-field-id", control.getGroupFieldId());
        setAttribute(element, "info-field-id", control.getInfoFieldId());
        setAttribute(element, "info-style", control.getInfoStyle());
        setAttribute(element, "mode", control.getMode().value);
        setControl(element, control);
        setField(element, control);
        setListField(element, control);
        setListQuery(element, control);
        setOptionsList(element, control.getOptions());
        return element;
    }

    @Override
    public Class<N2oGroupClassifierMulti> getElementClass() {
        return N2oGroupClassifierMulti.class;
    }

    @Override
    public String getElementName() {
        return "group-classifier";
    }
}
