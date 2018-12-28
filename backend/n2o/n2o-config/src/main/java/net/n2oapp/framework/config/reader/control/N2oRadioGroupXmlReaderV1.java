package net.n2oapp.framework.config.reader.control;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.control.list.N2oRadioGroup;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;

@Component
public class N2oRadioGroupXmlReaderV1 extends N2oStandardControlReaderV1<N2oRadioGroup> {
    @Override
    public N2oRadioGroup read(Element element, Namespace namespace) {
        N2oRadioGroup n2oRadioGroup = new N2oRadioGroup();
        n2oRadioGroup.setType(getAttributeString(element, "type"));
        return getQueryFieldDefinition(element, n2oRadioGroup);
    }

    @Override
    public Class<N2oRadioGroup> getElementClass() {
        return N2oRadioGroup.class;
    }

    @Override
    public String getElementName() {
        return "radio-group";
    }
}
