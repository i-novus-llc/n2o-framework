package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.N2oCustomField;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class CustomFieldIOv2 extends FieldIOv2<N2oCustomField> {

    @Override
    public void io(Element e, N2oCustomField m, IOProcessor p) {
        super.io(e, m, p);
    }

    @Override
    public Class<N2oCustomField> getElementClass() {
        return N2oCustomField.class;
    }

    @Override
    public String getElementName() {
        return "field";
    }
}
