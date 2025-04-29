package net.n2oapp.framework.config.io.control.v2.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oOutputText;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента вывода однострочного текста
 */
@Component
public class OutputTextIOv2 extends PlainFieldIOv2<N2oOutputText> {

    @Override
    public void io(Element e, N2oOutputText m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attribute(e, "format", m::getFormat, m::setFormat);
        p.attributeEnum(e, "position", m::getIconPosition, m::setIconPosition, PositionEnum.class);
    }

    @Override
    public Class<N2oOutputText> getElementClass() {
        return N2oOutputText.class;
    }

    @Override
    public String getElementName() {
        return "output-text";
    }
}