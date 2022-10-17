package net.n2oapp.framework.config.io.cell.v2;


import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oActionCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import org.jdom2.Element;

/**
 * Чтение\запись абстрактной ячейки с действиями версии 2.0
 */
public abstract class AbstractActionCellElementIOv2<T extends N2oActionCell> extends AbstractCellElementIOv2<T> {

    @Override
    public void io(Element e, T c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "action-id", c::getActionId, c::setActionId);
        p.anyChildren(e, actionSequenceTag(), c::getActions, c::setActions, p.anyOf(N2oAction.class)
                .ignore("input-text", "field", "date-time", "input-select"), ActionIOv1.NAMESPACE);
    }

    public abstract String actionSequenceTag();
}
