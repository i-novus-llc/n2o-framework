package net.n2oapp.framework.config.reader.control;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.control.plain.N2oCheckbox;
import org.springframework.stereotype.Component;

@Component
public class N2oCheckboxXmlReaderV1 extends N2oStandardControlReaderV1<N2oCheckbox> {
    @Override
    public N2oCheckbox read(Element element, Namespace namespace) {
        N2oCheckbox checkbox = new N2oCheckbox();
        getControlFieldDefinition(element, checkbox);
        return checkbox;
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
