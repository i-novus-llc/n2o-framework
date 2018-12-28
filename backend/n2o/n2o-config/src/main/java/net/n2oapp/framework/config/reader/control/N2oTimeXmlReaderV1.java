package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.plain.N2oTime;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * @author V. Alexeev.
 */
@Component
public class N2oTimeXmlReaderV1 extends N2oStandardControlReaderV1<N2oTime> {

    @Override
    public String getElementName() {
        return "time";
    }

    @Override
    public Class<N2oTime> getElementClass() {
        return N2oTime.class;
    }

    @Override
    public N2oTime read(Element element, Namespace namespace) {
        N2oTime time = new N2oTime();
        getControlFieldDefinition(element, time);
        return time;
    }
}
