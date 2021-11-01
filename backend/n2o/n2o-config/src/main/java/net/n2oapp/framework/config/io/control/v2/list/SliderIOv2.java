package net.n2oapp.framework.config.io.control.v2.list;

import net.n2oapp.framework.api.metadata.control.list.N2oSlider;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись атрибутов ползунка
 */
@Component
public class SliderIOv2 extends ListFieldIOv2<N2oSlider> {
    @Override
    public void io(Element e, N2oSlider m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeEnum(e, "mode", m::getMode, m::setMode, N2oSlider.Mode.class);
        p.attributeBoolean(e, "vertical", m::getVertical, m::setVertical);
        p.attribute(e, "measure", m::getMeasure, m::setMeasure);
        p.attributeInteger(e, "min", m::getMin, m::setMin);
        p.attributeInteger(e, "max", m::getMax, m::setMax);
        p.attributeInteger(e, "step", m::getStep, m::setStep);
    }

    @Override
    public Class<N2oSlider> getElementClass() {
        return N2oSlider.class;
    }

    @Override
    public String getElementName() {
        return "slider";
    }
}
