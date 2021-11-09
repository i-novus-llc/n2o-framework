package net.n2oapp.framework.config.io.control.v3.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oProgress;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента отображения прогресса версии 3.0
 */
@Component
public class ProgressIOv3 extends PlainFieldIOv3<N2oProgress> {

    @Override
    public void io(Element e, N2oProgress m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeInteger(e, "max", m::getMax, m::setMax);
        p.attribute(e, "bar-text", m::getBarText, m::setBarText);
        p.attributeBoolean(e, "animated", m::getAnimated, m::setAnimated);
        p.attributeBoolean(e, "striped", m::getStriped, m::setStriped);
        p.attribute(e, "color", m::getColor, m::setColor);
        p.attribute(e, "bar-class", m::getBarClass, m::setBarClass);
    }

    @Override
    public Class<N2oProgress> getElementClass() {
        return N2oProgress.class;
    }

    @Override
    public String getElementName() {
        return "progress";
    }
}
