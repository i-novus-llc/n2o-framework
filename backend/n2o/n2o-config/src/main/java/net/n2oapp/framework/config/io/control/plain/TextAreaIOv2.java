package net.n2oapp.framework.config.io.control.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oTextArea;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента ввода многострочного текста
 */
@Component
public class TextAreaIOv2 extends PlainFieldIOv2<N2oTextArea> {

    @Override
    public void io(Element e, N2oTextArea m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeInteger(e, "rows", m::getRows, m::setRows);
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
