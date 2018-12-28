package net.n2oapp.framework.config.reader.control;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.control.list.N2oCheckboxButtons;
import net.n2oapp.framework.api.metadata.control.list.N2oRadioButtons;
import net.n2oapp.framework.config.reader.MetadataReaderException;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.springframework.stereotype.Component;

/**
 * @author dfirstov
 * @since 06.02.2015
 */
@Component
public class N2oButtonsXmlReaderV1 extends N2oStandardControlReaderV1<N2oListField> {
    @Override
    public String getElementName() {
        return "buttons";
    }

    @Override
    public N2oListField read(Element element, Namespace namespace) {
        String dataToggle = ReaderJdomUtil.getAttributeString(element, "data-toggle");
        N2oListField buttons = new N2oRadioButtons();
        if (dataToggle.equals("buttons-radio")) {
            buttons = new N2oRadioButtons();
            ((N2oRadioButtons)buttons).setColorFieldId(ReaderJdomUtil.getAttributeString(element, "color-field-id"));
        } else if (dataToggle.equals("buttons-checkbox")) {
            buttons = new N2oCheckboxButtons();
            ((N2oCheckboxButtons)buttons).setColorFieldId(ReaderJdomUtil.getAttributeString(element, "color-field-id"));
        }
        return getQueryFieldDefinition(element, buttons);
    }

    @Override
    public Class<N2oListField> getElementClass() {
        return N2oListField.class;
    }
}
