package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.interval.N2oTimeInterval;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * @author V. Alexeev.
 */
@Component
public class N2oTimeIntervalXmlReaderV1 extends N2oStandardControlReaderV1<N2oTimeInterval> {

    @Override
    public String getElementName() {
        return "time-interval";
    }

    @Override
    public Class<N2oTimeInterval> getElementClass() {
        return N2oTimeInterval.class;
    }

    @Override
    public N2oTimeInterval read(Element element, Namespace namespace) {
        N2oTimeInterval interval = new N2oTimeInterval();
        getControlFieldDefinition(element, interval);
        return interval;
    }
}
