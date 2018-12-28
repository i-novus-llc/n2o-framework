package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.N2oCustomField;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

@Component
public class N2oCustomXmlReaderV1 extends N2oStandardControlReaderV1<N2oCustomField> {
    @Override
    public N2oCustomField read(Element element, Namespace namespace) {
        N2oCustomField n2oCustomField = new N2oCustomField();
        getControlFieldDefinition(element, n2oCustomField);
        return n2oCustomField;
    }

    @Override
    public Class<N2oCustomField> getElementClass() {
        return N2oCustomField.class;
    }

    @Override
    public String getElementName() {
        return "custom";
    }
}
