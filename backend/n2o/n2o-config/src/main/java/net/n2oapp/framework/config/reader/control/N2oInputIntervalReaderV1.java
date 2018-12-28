package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.interval.N2oInputInterval;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeInteger;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;

/**
 * @author iryabov
 * @since 21.02.2017
 */
@Component
public class N2oInputIntervalReaderV1 extends N2oStandardControlReaderV1<N2oInputInterval> {

    @Override
    public String getElementName() {
        return "input-interval";
    }

    @Override
    public Class<N2oInputInterval> getElementClass() {
        return N2oInputInterval.class;
    }

    @Override
    public N2oInputInterval read(Element element, Namespace namespace) {
        N2oInputInterval interval = new N2oInputInterval();
        getControlFieldDefinition(element, interval);
        interval.setMax(getAttributeInteger(element, "max"));
        interval.setMin(getAttributeInteger(element, "min"));
        interval.setStep(getAttributeString(element, "step"));
        return interval;
    }
}
