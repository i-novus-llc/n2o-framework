package net.n2oapp.framework.config.io.control.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oTextArea;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class TextAreaIOv2 extends PlainFieldIOv2<N2oTextArea> {

    @Override
    public void io(Element e, N2oTextArea m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "height", m::getHeight, m::setHeight);
        p.attributeInteger(e, "rows", m::getRows, m::setRows);
        p.attribute(e, "placeholder", m::getPlaceholder, m::setPlaceholder);
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
