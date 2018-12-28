package net.n2oapp.framework.config.io.widget.table.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oProgressBarCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.Size;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 *Чтение\запись ячейки с индикатором прогресса
 */
@Component
public class ProgressCellElementIOv2 extends AbstractCellElementIOv2<N2oProgressBarCell> {
    @Override
    public void io(Element e, N2oProgressBarCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "value", c::getValue, c::setValue);
        p.attributeBoolean(e, "active", c::getActive, c::setActive);
        p.attributeEnum(e, "size", c::getSize, c::setSize, Size.class);
        p.attributeBoolean(e, "striped", c::getStriped, c::setStriped);
        p.attribute(e, "color", c::getColor, c::setColor);
    }

    @Override
    public String getElementName() {
        return "progress" ;
    }

    @Override
    public Class<N2oProgressBarCell> getElementClass() {
        return N2oProgressBarCell.class;
    }
}
