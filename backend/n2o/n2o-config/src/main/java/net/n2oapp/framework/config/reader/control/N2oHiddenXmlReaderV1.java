package net.n2oapp.framework.config.reader.control;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.control.N2oHidden;
import org.springframework.stereotype.Component;

@Component
public class N2oHiddenXmlReaderV1 extends N2oStandardControlReaderV1<N2oHidden> {
    @Override
    public N2oHidden read(Element element, Namespace namespace) {
        N2oHidden n2oHidden = new N2oHidden();
        getControlFieldDefinition(element, n2oHidden);
        return n2oHidden;
    }

    @Override
    public Class<N2oHidden> getElementClass() {
        return N2oHidden.class;
    }

    @Override
    public String getElementName() {
        return "hidden";
    }

}
