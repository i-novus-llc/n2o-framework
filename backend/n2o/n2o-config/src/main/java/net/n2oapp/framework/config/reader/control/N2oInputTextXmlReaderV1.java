package net.n2oapp.framework.config.reader.control;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.*;

@Component
public class N2oInputTextXmlReaderV1 extends N2oStandardControlReaderV1<N2oInputText> {
    @Override
    public N2oInputText read(Element element, Namespace namespace) {
        N2oInputText n2oInputText = new N2oInputText();
        getControlFieldDefinition(element, n2oInputText);
        n2oInputText.setLength(getAttributeInteger(element, "length"));
        n2oInputText.setMax(getAttributeString(element, "max"));
        n2oInputText.setMin(getAttributeString(element, "min"));
        n2oInputText.setStep(getAttributeString(element, "step"));
        n2oInputText.setNamespaceUri(element.getNamespaceURI());
        return n2oInputText;
    }

    @Override
    public Class<N2oInputText> getElementClass() {
        return N2oInputText.class;
    }

    @Override
    public String getElementName() {
        return "input-text";
    }
}
