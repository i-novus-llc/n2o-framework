package net.n2oapp.framework.config.io.control.v2.plain;

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
        p.read(e, m, (el, mo) -> {
            if (el.getAttribute("rows") != null)
                mo.setMaxRows(Integer.parseInt(el.getAttributeValue("rows")));
        }); //deprecated
        p.attributeInteger(e, "min-rows", m::getMinRows, m::setMinRows);
        p.attributeInteger(e, "max-rows", m::getMaxRows, m::setMaxRows);
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
