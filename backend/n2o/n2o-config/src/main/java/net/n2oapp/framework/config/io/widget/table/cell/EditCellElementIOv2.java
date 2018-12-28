package net.n2oapp.framework.config.io.widget.table.cell;

import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.global.view.widget.table.EditType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oXEditableCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 *Чтение\запись ячейки с редактированием значения.
 */
@Component
public class EditCellElementIOv2 extends AbstractCellElementIOv2<N2oXEditableCell>  {

    @Override
    public void io(Element e, N2oXEditableCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "invoke-action-id", c::getActionId, c::setActionId);
        p.attributeEnum(e, "type", c::getEditType, c::setEditType, EditType.class);
        p.anyChild(e, null, c::getN2oField, c::setN2oField, p.anyOf(N2oField.class), CellIOv2.NAMESPACE);
        //todo:добавить поле ввода
    }

    @Override
    public String getElementName() {
        return "edit" ;
    }

    @Override
    public Class<N2oXEditableCell> getElementClass() {
        return N2oXEditableCell.class;
    }
}

