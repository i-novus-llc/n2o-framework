package net.n2oapp.framework.config.reader.control;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.control.plain.N2oMaskedInput;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.springframework.stereotype.Component;

@Component
public class N2oMaskedInputXmlReaderV1 extends N2oStandardControlReaderV1<N2oMaskedInput> {
    @Override
    public N2oMaskedInput read(Element element, Namespace namespace) {
        N2oMaskedInput maskedInput = new N2oMaskedInput();
        getControlFieldDefinition(element, maskedInput);
        maskedInput.setMask(ReaderJdomUtil.getAttributeString(element, "mask"));
        return maskedInput;
    }

    @Override
    public Class<N2oMaskedInput> getElementClass() {
        return N2oMaskedInput.class;
    }

    @Override
    public String getElementName() {
        return "masked-input";
    }
}
