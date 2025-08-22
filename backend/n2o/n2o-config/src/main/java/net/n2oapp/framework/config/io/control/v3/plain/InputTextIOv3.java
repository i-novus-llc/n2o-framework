package net.n2oapp.framework.config.io.control.v3.plain;


import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение, запись компонента ввода текста версии 3.0
 */
@Component
public class InputTextIOv3 extends PlainFieldIOv3<N2oInputText> {

    @Override
    public void io(Element e, N2oInputText m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeInteger(e, "length", m::getLength, m::setLength);
        p.attribute(e, "min", m::getMin, m::setMin);
        p.attribute(e, "max", m::getMax, m::setMax);
        p.attribute(e, "step", m::getStep, m::setStep);
        p.attribute(e, "measure", m::getMeasure, m::setMeasure);
        p.attributeInteger(e, "precision", m::getPrecision, m::setPrecision);
        p.attribute(e, "autocomplete", m::getAutocomplete, m::setAutocomplete);
    }

    @Override
    public Class<N2oInputText> getElementClass() {
        return N2oInputText.class;
    }

    @Override
    public String getElementName() {
        return "input-text";
    }
}
