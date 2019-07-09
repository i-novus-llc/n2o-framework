package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.list.N2oSlider;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.*;

/**
 * Считывает N2oSlider из xml-файла
 */
@Component
public class N2oSliderXmlReaderV1 extends N2oStandardControlReaderV1<N2oSlider> {
    @Override
    public N2oSlider read(Element element, Namespace namespace) {
        N2oSlider slider = new N2oSlider();
        slider.setMode(getAttributeEnum(element, "mode", N2oSlider.Mode.class));
        slider.setVertical(getAttributeBoolean(element, "vertical"));
        slider.setMeasure(getAttributeString(element, "measure"));
        slider.setMin(getAttributeInteger(element, "min"));
        slider.setMax(getAttributeInteger(element, "max"));
        slider.setStep(getAttributeInteger(element, "step"));
        return slider;
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
