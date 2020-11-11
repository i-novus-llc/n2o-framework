package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.N2oTimePicker;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение запись компонента ввода времени
 */
@Component
public class TimePickerIOv2 extends ComponentIO<N2oTimePicker> implements ControlIOv2 {

    @Override
    public void io(Element e, N2oTimePicker m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "id", m::getId, m::setId);
        p.attribute(e, "prefix", m::getPrefix, m::setPrefix);
        p.attributeArray(e, "mode", ",", m::getMode, m::setMode);
        p.attribute(e, "data-format", m::getDataFormat, m::setDataFormat);
        p.attribute(e, "format", m::getFormat, m::setFormat);
        p.attribute(e, "default-value", m::getDefaultValue, m::setDefaultValue);
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
