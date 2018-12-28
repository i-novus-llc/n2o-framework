package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.N2oButtonField;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * Сохраняет N2oButtonField в xml-файл
 */
@Component
public class N2oButtonFieldPersister extends N2oControlXmlPersister<N2oButtonField> {

    @Override
    public Element persist(N2oButtonField buttonField, Namespace namespace) {
        Element element = new Element(getElementName(), namespacePrefix, namespaceUri);
        setControl(element, buttonField);
        setField(element, buttonField);
        PersisterJdomUtil.setAttribute(element, "title", buttonField.getTitle());
        PersisterJdomUtil.setAttribute(element, "title-field-id", buttonField.getTitleFieldId());
        PersisterJdomUtil.setAttribute(element, "icon", buttonField.getIcon());
        PersisterJdomUtil.setAttribute(element, "icon-field-id", buttonField.getIconFieldId());
        PersisterJdomUtil.setAttribute(element, "type", buttonField.getType());
        if(buttonField.getEvent() != null) {
            Element event = new Element("event", element.getNamespace());
            Element eventElement = persisterFactory.produce(buttonField.getEvent()).persist(buttonField.getEvent(),namespace);
            PersisterJdomUtil.installPrefix(eventElement, event);
            event.addContent(eventElement);
            element.addContent(event);
        }
        return element;
    }

    @Override
    public Class<N2oButtonField> getElementClass() {
        return N2oButtonField.class;
    }

    @Override
    public String getElementName() {
        return "button";
    }
}
