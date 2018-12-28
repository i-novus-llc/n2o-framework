package net.n2oapp.framework.config.persister.control;

import org.jdom.Element;
import net.n2oapp.framework.api.metadata.control.plain.N2oCheckbox;
import org.springframework.stereotype.Component;
import org.jdom.Namespace;

/**
 * User: iryabov
 * Date: 20.11.13
 * Time: 9:49
 */
@Component
public class N2oCheckBoxPersister extends N2oControlXmlPersister<N2oCheckbox> {
    @Override
    public Element persist(N2oCheckbox control,Namespace namespace) {
        Element element = new Element(getElementName(), namespacePrefix, namespaceUri);
        setControl(element, control);
        setField(element, control);
        return element;
    }

    @Override
    public Class<N2oCheckbox> getElementClass() {
        return N2oCheckbox.class;
    }

    @Override
    public String getElementName() {
        return "checkbox";
    }
}
