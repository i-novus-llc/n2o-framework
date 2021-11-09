package net.n2oapp.framework.config.io.control.v3.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oRating;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение, запись компонента ввода рейтинга версии 3.0
 */
@Component
public class RatingIOv3 extends PlainFieldIOv3<N2oRating> {

    @Override
    public void io(Element e, N2oRating m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeInteger(e, "max", m::getMax, m::setMax);
        p.attributeBoolean(e, "half", m::getHalf, m::setHalf);
        p.attributeBoolean(e, "show-tooltip", m::getShowTooltip, m::setShowTooltip);
    }

    @Override
    public Class<N2oRating> getElementClass() {
        return N2oRating.class;
    }

    @Override
    public String getElementName() {
        return "rating";
    }
}
