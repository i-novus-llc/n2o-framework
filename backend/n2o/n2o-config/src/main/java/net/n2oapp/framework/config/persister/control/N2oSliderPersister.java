package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.list.N2oSlider;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * Сохранение N2oSlider в Xml-файл
 */
@Component
public class N2oSliderPersister extends N2oControlXmlPersister<N2oSlider> {
    @Override
    public Element persist(N2oSlider slider, Namespace namespace) {
        Element element = new Element(getElementName(), namespacePrefix, namespaceUri);
        setControl(element, slider);
        setField(element, slider);
        setListField(element, slider);
        setListQuery(element, slider);
        setOptionsList(element, slider.getOptions());
        setAttribute(element, "mode", slider.getMode());
        setAttribute(element, "vertical", slider.isVertical());
        setAttribute(element, "measure", slider.getMeasure());
        setAttribute(element, "min", slider.getMin());
        setAttribute(element, "max", slider.getMax());
        setAttribute(element, "step", slider.getStep());
        return element;
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
