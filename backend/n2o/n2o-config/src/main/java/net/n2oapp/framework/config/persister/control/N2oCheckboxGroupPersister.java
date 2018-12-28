package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.multi.N2oCheckboxGroup;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * User: iryabov
 * Date: 20.11.13
 * Time: 9:31
 */
@Component
public class N2oCheckboxGroupPersister extends N2oControlXmlPersister<N2oCheckboxGroup> {
    @Override
    public Element persist(N2oCheckboxGroup control, Namespace namespace) {
        Element element = new Element(getElementName(), namespacePrefix, namespaceUri);
        PersisterJdomUtil.setAttribute(element, "inline", control.getInline());
        setControl(element, control);
        setField(element, control);
        setListField(element, control);
        setListQuery(element, control);
        setOptionsList(element, control.getOptions());
        return element;
    }

    @Override
    public Class<N2oCheckboxGroup> getElementClass() {
        return N2oCheckboxGroup.class;
    }

    @Override
    public String getElementName() {
        return "checkbox-group";
    }
}
