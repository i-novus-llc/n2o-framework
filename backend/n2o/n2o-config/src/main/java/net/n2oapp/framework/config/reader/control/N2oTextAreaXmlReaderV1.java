package net.n2oapp.framework.config.reader.control;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.control.plain.N2oTextArea;
import org.springframework.stereotype.Component;

@Component
public class N2oTextAreaXmlReaderV1 extends N2oStandardControlReaderV1<N2oTextArea> {
    @Override
    public N2oTextArea read(Element element, Namespace namespace) {
        N2oTextArea textArea = new N2oTextArea();
        readControlTextDefinition(element, textArea);
        return textArea;
    }

    @Override
    public Class<N2oTextArea> getElementClass() {
        return N2oTextArea.class;
    }

    @Override
    public String getElementName() {
        return "text-area";
    }
}
