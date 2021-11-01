package net.n2oapp.framework.config.io.cell.v3;


import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oListCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись ячейки со списком
 */
@Component
public class ListCellElementIOv3 extends AbstractCellElementIOv3<N2oListCell> {

    @Override
    public void io(Element e, N2oListCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "color", c::getColor, c::setColor);
        p.attribute(e, "label-field-id", c::getLabelFieldId, c::setLabelFieldId);
        p.attributeInteger(e, "size", c::getSize, c::setSize);
        p.child(e, null, "switch", c::getN2oSwitch, c::setN2oSwitch, new SwitchIOv3());
    }

    @Override
    public String getElementName() {
        return "list";
    }

    @Override
    public Class<N2oListCell> getElementClass() {
        return N2oListCell.class;
    }
}
