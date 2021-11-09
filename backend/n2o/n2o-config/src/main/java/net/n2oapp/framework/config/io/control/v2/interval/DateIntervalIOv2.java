package net.n2oapp.framework.config.io.control.v2.interval;

import net.n2oapp.framework.api.metadata.control.interval.N2oDateInterval;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

@Component
public class DateIntervalIOv2 extends BaseIntervalFieldIOv2<N2oDateInterval> {

    @Override
    public void io(Element e, N2oDateInterval m, IOProcessor p) {
        super.io(e, m, p);
        p.read(e, m, (el,mo) -> mo.setDateFormat(el.getAttributeValue("format")));//deprecated
        p.attribute(e, "date-format", m::getDateFormat, m::setDateFormat);
        p.attribute(e, "time-format", m::getTimeFormat, m::setTimeFormat);
        p.attribute(e, "begin-default-time", m::getBeginDefaultTime, m::setBeginDefaultTime);
        p.attribute(e, "end-default-time", m::getEndDefaultTime, m::setEndDefaultTime);
        p.attribute(e, "max", m::getMax, m::setMax);
        p.attribute(e, "min", m::getMin, m::setMin);
        p.attributeBoolean(e, "utc", m::getUtc, m::setUtc);
    }

    @Override
    public Class getElementClass() {
        return N2oDateInterval.class;
    }

    @Override
    public String getElementName() {
        return "date-interval";
    }
}
