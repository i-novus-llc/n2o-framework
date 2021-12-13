package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.control.N2oText;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись базовых свойств компонента TextField версии 3.0
 */

@Component
public class TextFieldIOv3 extends FieldIOv3<N2oText> implements ControlIOv3 {

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
