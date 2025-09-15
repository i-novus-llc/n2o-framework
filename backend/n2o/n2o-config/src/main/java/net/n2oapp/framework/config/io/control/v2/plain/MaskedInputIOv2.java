package net.n2oapp.framework.config.io.control.v2.plain;

import net.n2oapp.framework.api.metadata.control.masked.N2oMaskedInput;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

@Component
public class MaskedInputIOv2 extends PlainFieldIOv2<N2oMaskedInput> {

    @Override
    public void io(Element e, N2oMaskedInput m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "mask", m::getMask, m::setMask);
        p.attribute(e, "measure", m::getMeasure, m::setMeasure);
        p.attributeBoolean(e, "clear-on-blur", m::getClearOnBlur, m::setClearOnBlur);
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
