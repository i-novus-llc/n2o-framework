package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.control.N2oStatus;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.Position;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.v2.ControlIOv2;
import net.n2oapp.framework.config.io.control.v2.FieldIOv2;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента отображения статуса версии 3.0
 */
@Component
public class StatusFieldIOv3 extends FieldIOv3<N2oStatus> implements ControlIOv3 {

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
