package net.n2oapp.framework.config.reader.control;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.control.plain.N2oDatePicker;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.springframework.stereotype.Component;

@Component
public class N2oDateTimeXmlReaderV1 extends N2oStandardControlReaderV1<N2oDatePicker> {
    @Override
    public N2oDatePicker read(Element element, Namespace namespace) {
        N2oDatePicker n2oDatePicker = new N2oDatePicker();
        n2oDatePicker.setDateFormat(ReaderJdomUtil.getAttributeString(element, "format"));
        n2oDatePicker.setDefaultTime(ReaderJdomUtil.getAttributeString(element, "default-time"));
        n2oDatePicker.setMax(ReaderJdomUtil.getAttributeString(element, "max"));
        n2oDatePicker.setMin(ReaderJdomUtil.getAttributeString(element, "min"));
        n2oDatePicker.setUtc(ReaderJdomUtil.getAttributeBoolean(element, "utc"));
        getControlFieldDefinition(element, n2oDatePicker);
        return n2oDatePicker;
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
