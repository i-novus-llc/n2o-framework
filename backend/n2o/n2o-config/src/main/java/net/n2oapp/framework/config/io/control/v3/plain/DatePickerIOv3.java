package net.n2oapp.framework.config.io.control.v3.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oDatePicker;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение запись компонента выбора даты версии 3.0
 */
@Component
public class DatePickerIOv3 extends PlainFieldIOv3<N2oDatePicker> {

    @Override
    public void io(Element e, N2oDatePicker m, IOProcessor p) {
        super.io(e, m, p);
        p.read(e, m, (el,mo) -> mo.setDateFormat(el.getAttributeValue("format")));//deprecated
        p.attribute(e, "date-format", m::getDateFormat, m::setDateFormat);
        p.attribute(e, "time-format", m::getTimeFormat, m::setTimeFormat);
        p.attribute(e, "default-time", m::getDefaultTime, m::setDefaultTime);
        p.attribute(e, "min", m::getMin, m::setMin);
        p.attribute(e, "max", m::getMax, m::setMax);
        p.attributeBoolean(e, "utc", m::getUtc, m::setUtc);
        p.attribute(e, "autocomplete", m::getAutocomplete, m::setAutocomplete);
    }

    @Override
    public Class<N2oDatePicker> getElementClass() {
        return N2oDatePicker.class;
    }

    @Override
    public String getElementName() {
        return "date-time";
    }
}
