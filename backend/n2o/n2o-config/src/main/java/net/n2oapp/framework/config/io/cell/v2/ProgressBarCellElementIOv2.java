package net.n2oapp.framework.config.io.cell.v2;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oProgressBarCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись ячейки с индикатором прогресса
 */
@Component
public class ProgressBarCellElementIOv2 extends AbstractCellElementIOv2<N2oProgressBarCell> {
    @Override
    public void io(Element e, N2oProgressBarCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attributeBoolean(e, "active", c::getActive, c::setActive);
        p.attributeEnum(e, "size", c::getSize, c::setSize, N2oProgressBarCell.Size.class);
        p.attributeBoolean(e, "striped", c::getStriped, c::setStriped);
        p.attribute(e, "color", c::getColor, c::setColor);
    }

    @Override
    public String getElementName() {
        return "progress";
    }

    @Override
    public Class<N2oProgressBarCell> getElementClass() {
        return N2oProgressBarCell.class;
    }
}
