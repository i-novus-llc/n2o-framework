package net.n2oapp.framework.config.io.cell.v2;

import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oEditCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.v2.ControlIOv2;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись ячейки с редактированием значения.
 */
@Component
public class EditCellElementIOv2 extends AbstractActionCellElementIOv2<N2oEditCell> {

    @Override
    public void io(Element e, N2oEditCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "format", c::getFormat, c::setFormat);
        p.attribute(e, "enabled", c::getEnabled, c::setEnabled);
        p.anyChild(e, null, c::getN2oField, c::setN2oField, p.anyOf(N2oField.class), ControlIOv2.NAMESPACE);
    }

    @Override
    public String getElementName() {
        return "edit";
    }

    @Override
    public Class<N2oEditCell> getElementClass() {
        return N2oEditCell.class;
    }
}

