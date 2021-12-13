package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.api.metadata.control.N2oText;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись базовых свойств компонента TextField
 */

@Component
public class TextFieldIOv2 extends FieldIOv2<N2oText> implements ControlIOv2 {

    @Override
    public void io(Element e, N2oText m, IOProcessor p) {
        super.io(e, m, p);
        p.text(e, m::getText, m::setText);
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
