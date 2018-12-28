package net.n2oapp.framework.config.io.control.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oMaskedInput;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class MaskedInputIOv2 extends PlainFieldIOv2<N2oMaskedInput> {

    @Override
    public void io(Element e, N2oMaskedInput m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "mask", m::getMask, m::setMask);
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
