package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.N2oButtonField;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * Считывает контрол button из xml-файла
 */
@Component
public class N2oButtonFieldXmlReaderV1 extends N2oStandardControlReaderV1<N2oButtonField> {
    @Override
    public String getElementName() {
        return "button";
    }

    @Override
    public Class<N2oButtonField> getElementClass() {
        return N2oButtonField.class;
    }

    @Override
    public N2oButtonField read(Element element, Namespace namespace) {
        N2oButtonField buttonField = new N2oButtonField();
        getControlFieldDefinition(element, buttonField);
        buttonField.setTitle(ReaderJdomUtil.getAttributeString(element, "title"));
        buttonField.setTitleFieldId(ReaderJdomUtil.getAttributeString(element, "title-field-id"));
        buttonField.setIcon(ReaderJdomUtil.getAttributeString(element, "icon"));
        buttonField.setIconFieldId(ReaderJdomUtil.getAttributeString(element, "icon-field-id"));
        buttonField.setType(ReaderJdomUtil.getAttributeEnum(element, "type", LabelType.class));
        Element eventElement = element.getChild("event", namespace);
        if (eventElement != null && eventElement.getChildren() != null && !eventElement.getChildren().isEmpty()) {
            N2oAction event = (N2oAction) readerFactory.produce((Element) eventElement.getChildren().get(0),
                    element.getNamespace(), DEFAULT_EVENT_NAMESPACE_URI).read((Element) eventElement.getChildren().get(0));
            buttonField.setAction(event);
        }
        return buttonField;
    }
}
