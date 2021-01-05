package net.n2oapp.framework.config.reader.control;

import org.jdom2.Element;
import org.jdom2.Namespace;
import net.n2oapp.framework.api.metadata.control.plain.N2oTextArea;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeInteger;

@Component
public class N2oTextAreaXmlReaderV1 extends N2oStandardControlReaderV1<N2oTextArea> {
    @Override
    public N2oTextArea read(Element element, Namespace namespace) {
        N2oTextArea textArea = new N2oTextArea();
        getControlFieldDefinition(element, textArea);
        textArea.setRows(getAttributeInteger(element, "rows"));
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
