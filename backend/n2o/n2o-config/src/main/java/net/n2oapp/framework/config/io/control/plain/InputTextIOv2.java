package net.n2oapp.framework.config.io.control.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

@Component
public class InputTextIOv2 extends PlainFieldIOv2<N2oInputText> {

    @Override
    public void io(Element e, N2oInputText m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeInteger(e, "length", m::getLength, m::setLength);
        p.attribute(e, "min", m::getMin, m::setMin);
        p.attribute(e, "max", m::getMax, m::setMax);
        p.attribute(e, "step", m::getStep, m::setStep);
        p.attribute(e, "measure", m::getMeasure, m::setMeasure);
        p.attribute(e, "ref-model", m::getRefModel, m::setRefModel);
        p.attribute(e, "ref-page", m::getRefPage, m::setRefPage);
        p.attribute(e, "ref-widget-id", m::getRefWidgetId, m::setRefWidgetId);
    }

    @Override
    public Class<N2oInputText> getElementClass() {
        return N2oInputText.class;
    }

    @Override
    public String getElementName() {
        return "input-text";
    }
}
