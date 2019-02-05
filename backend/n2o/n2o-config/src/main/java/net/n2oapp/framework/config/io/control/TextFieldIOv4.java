package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.N2oText;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись базовых свойств компонента TextField
 */

@Component
public class TextFieldIOv4 extends ComponentIO<N2oText> implements ControlIOv2 {

    @Override
    public void io(Element e, N2oText m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "text", m::getText, m::setText);
        p.attribute(e, "format", m::getFormat, m::setFormat);
    }

    @Override
    public Class<N2oText> getElementClass() {
        return N2oText.class;
    }

    @Override
    public String getElementName() {
        return "text";
    }
}
