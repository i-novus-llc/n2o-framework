package net.n2oapp.framework.config.io.control.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oOutputText;
import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.Position;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class OutputTextIOv2 extends PlainFieldIOv2<N2oOutputText> {

    @Override
    public void io(Element e, N2oOutputText m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attribute(e, "format", m::getFormat, m::setFormat);
        p.attributeEnum(e, "type", m::getType, m::setType, IconType.class);
        p.attributeEnum(e, "position", m::getPosition, m::setPosition, Position.class);

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