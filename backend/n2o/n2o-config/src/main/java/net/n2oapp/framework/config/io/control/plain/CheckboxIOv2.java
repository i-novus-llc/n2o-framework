package net.n2oapp.framework.config.io.control.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oCheckbox;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class CheckboxIOv2 extends PlainFieldIOv2<N2oCheckbox> {
    @Override
    public Class getElementClass() {
        return N2oCheckbox.class;
    }

    @Override
    public String getElementName() {
        return "checkbox";
    }

    @Override
    public void io(Element e, N2oCheckbox m, IOProcessor p) {
        super.io(e, m, p);
    }
}
