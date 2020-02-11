package net.n2oapp.framework.config.io.widget.table.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oAbstractCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oSwitchCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись переключаемой ячейки
 */
@Component
public class SwitchCellElementIOv2 extends AbstractCellElementIOv2<N2oSwitchCell> {

    @Override
    public void io(Element e, N2oSwitchCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "value-field-id", c::getValueFieldId, c::setValueFieldId);
        p.children(e, null, "case", c::getCases, c::setCases, N2oSwitchCell.Case.class, this::cases);
        p.anyChild(e, "default", c::getDefaultCase, c::setDefaultCase, p.anyOf(N2oAbstractCell.class), CellIOv2.NAMESPACE);
    }

    private void cases(Element e, N2oSwitchCell.Case c, IOProcessor p) {
        p.attribute(e, "value", c::getValue, c::setValue);
        p.anyChild(e, null, c::getCell, c::setCell, p.anyOf(N2oAbstractCell.class), CellIOv2.NAMESPACE);
    }

    @Override
    public Class<N2oSwitchCell> getElementClass() {
        return N2oSwitchCell.class;
    }

    @Override
    public String getElementName() {
        return "switch";
    }
}
