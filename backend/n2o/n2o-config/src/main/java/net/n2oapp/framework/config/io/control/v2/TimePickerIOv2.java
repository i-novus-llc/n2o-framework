package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.api.metadata.control.N2oTimePicker;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.plain.PlainFieldIOv2;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение запись компонента ввода времени
 */
@Component
public class TimePickerIOv2 extends PlainFieldIOv2<N2oTimePicker> implements ControlIOv2 {

    @Override
    public void io(Element e, N2oTimePicker m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "prefix", m::getPrefix, m::setPrefix);
        p.attributeArray(e, "mode", ",", m::getMode, m::setMode);
        p.attribute(e, "time-format", m::getTimeFormat, m::setTimeFormat);
        p.attribute(e, "format", m::getFormat, m::setFormat);
    }

    @Override
    public Class<N2oTimePicker> getElementClass() {
        return N2oTimePicker.class;
    }

    @Override
    public String getElementName() {
        return "time-picker";
    }

}
