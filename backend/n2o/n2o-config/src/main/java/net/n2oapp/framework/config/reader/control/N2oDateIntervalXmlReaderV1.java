package net.n2oapp.framework.config.reader.control;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.control.interval.N2oDateInterval;
import net.n2oapp.framework.api.metadata.control.properties.PopupPlacement;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;
@Component
public class N2oDateIntervalXmlReaderV1 extends N2oStandardControlReaderV1<N2oDateInterval> {
    @Override
    public N2oDateInterval read(Element element, Namespace namespace) {
        N2oDateInterval dateInterval = new N2oDateInterval();
        dateInterval.setDateFormat(getAttributeString(element, "format"));
        dateInterval.setBeginDefaultTime(getAttributeString(element, "begin-default-time"));
        dateInterval.setEndDefaultTime(getAttributeString(element, "end-default-time"));
        dateInterval.setPopupPlacement(ReaderJdomUtil.getAttributeEnum(element, "popup-placement", PopupPlacement.class));
        dateInterval.setUtc(ReaderJdomUtil.getAttributeBoolean(element, "utc"));
        getControlFieldDefinition(element, dateInterval);
        return dateInterval;
    }

    @Override
    public Class<N2oDateInterval> getElementClass() {
        return N2oDateInterval.class;
    }

    @Override
    public String getElementName() {
        return "date-interval";
    }
}
