package net.n2oapp.framework.config.io.cell.v2;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractSwitch;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oAbstractCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oSwitchCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись переключателя ячеек
 */
@Component
public class SwitchCellElementIOv2 implements CellIOv2, NamespaceIO<N2oSwitchCell> {

    @Override
    public void io(Element e, N2oSwitchCell c, IOProcessor p) {
        p.attribute(e, "value-field-id", c::getValueFieldId, c::setValueFieldId);
        p.children(e, null, "case", c::getCases, c::setCases, N2oAbstractSwitch.Case.class, this::caseItem);
        p.anyChild(e, "default", c::getDefaultCase, c::setDefaultCase, p.anyOf(N2oAbstractCell.class), getNamespace());
    }

    protected void caseItem(Element e, N2oSwitchCell.Case<N2oAbstractCell> c, IOProcessor p) {
        p.attribute(e, "value", c::getValue, c::setValue);
        p.anyChild(e, null, c::getItem, c::setItem, p.anyOf(N2oAbstractCell.class), getNamespace());
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
