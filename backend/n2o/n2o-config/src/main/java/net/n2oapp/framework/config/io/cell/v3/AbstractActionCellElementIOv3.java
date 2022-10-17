package net.n2oapp.framework.config.io.cell.v3;


import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oActionCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.v2.ActionIOv2;
import org.jdom2.Element;

/**
 * Чтение\запись абстрактной ячейки с действиями версии 3.0
 */
public abstract class AbstractActionCellElementIOv3<T extends N2oActionCell> extends AbstractCellElementIOv3<T>  {

    @Override
    public void io(Element e, T c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "action-id", c::getActionId, c::setActionId);
        p.anyChildren(e, actionsSequenceTag(), c::getActions, c::setActions, p.anyOf(N2oAction.class), ActionIOv2.NAMESPACE);
    }

    public abstract String actionsSequenceTag();
}
