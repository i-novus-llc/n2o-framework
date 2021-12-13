package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.api.metadata.control.N2oStatus;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.Position;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента отображения статуса
 */
@Component
public class StatusFieldIOv2 extends FieldIOv2<N2oStatus> implements ControlIOv2 {

    @Override
    public void io(Element e, N2oStatus m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "color", m::getColor, m::setColor);
        p.attribute(e, "text", m::getText, m::setText);
        p.attributeEnum(e, "text-position", m::getTextPosition, m::setTextPosition, Position.class);
    }

    @Override
    public Class<N2oStatus> getElementClass() {
        return N2oStatus.class;
    }

    @Override
    public String getElementName() {
        return "status";
    }
}
