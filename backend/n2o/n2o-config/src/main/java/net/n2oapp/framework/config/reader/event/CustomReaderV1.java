package net.n2oapp.framework.config.reader.event;

import net.n2oapp.framework.api.metadata.event.action.N2oCustomAction;
import net.n2oapp.framework.config.reader.tools.PropertiesReaderV1;
import org.jdom.Element;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;

/**
 * Считывание переопределенного эвента
 */
@Component
public class CustomReaderV1 extends AbstractN2oEventXmlReader<N2oCustomAction> {

    @Override
    public N2oCustomAction read(Element element) {
        if (element == null) return null;
        N2oCustomAction custom = new N2oCustomAction();
        custom.setSrc(getAttributeString(element, "src"));
        custom.setProperties(PropertiesReaderV1.getInstance().read(element, element.getNamespace()));
        custom.setNamespaceUri(getNamespaceUri());
        return custom;
    }

    @Override
    public Class<N2oCustomAction> getElementClass() {
        return N2oCustomAction.class;
    }

    @Override
    public String getElementName() {
        return "custom";
    }
}
