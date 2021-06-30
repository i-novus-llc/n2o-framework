package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.list.N2oRadioGroup;
import org.jdom2.Element;
import org.jdom2.Namespace;
import net.n2oapp.framework.api.metadata.control.multi.N2oCheckboxGroup;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeEnum;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;

@Component
public class N2oCheckboxGroupXmlReaderV1 extends N2oStandardControlReaderV1<N2oCheckboxGroup> {
    @Override
    public N2oCheckboxGroup read(Element element, Namespace namespace) {
        N2oCheckboxGroup n2oCheckboxGroup = new N2oCheckboxGroup();
        n2oCheckboxGroup.setType(getAttributeEnum(element, "type", N2oCheckboxGroup.CheckboxGroupType.class));
        return getQueryFieldDefinition(element, n2oCheckboxGroup);
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
