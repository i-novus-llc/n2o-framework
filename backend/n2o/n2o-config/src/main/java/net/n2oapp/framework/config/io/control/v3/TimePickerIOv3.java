package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.control.N2oTimePicker;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.v3.plain.PlainFieldIOv3;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение запись компонента ввода времени версии 3.0
 */
@Component
public class TimePickerIOv3 extends PlainFieldIOv3<N2oTimePicker> implements ControlIOv3 {

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
