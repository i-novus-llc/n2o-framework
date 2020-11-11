package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.N2oNumberPicker;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента выбора числа из диапазона
 */
@Component
public class NumberPickerIOv2 extends FieldIOv2<N2oNumberPicker> {

    @Override
    public void io(Element e, N2oNumberPicker m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeInteger(e, "min", m::getMin, m::setMin);
        p.attributeInteger(e, "max", m::getMax, m::setMax);
        p.attributeInteger(e, "step", m::getStep, m::setStep);
        p.attribute(e, "default-value", m::getDefaultValue, m::setDefaultValue);
    }

    @Override
    public Class<N2oNumberPicker> getElementClass() {
        return N2oNumberPicker.class;
    }

    @Override
    public String getElementName() {
        return "number-picker";
    }
}
