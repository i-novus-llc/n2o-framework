package net.n2oapp.framework.config.io.cell.v2;


import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oActionCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import net.n2oapp.framework.config.io.common.ActionsAwareIO;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Чтение\запись абстрактной ячейки с действиями версии 2.0
 */
public abstract class AbstractActionCellElementIOv2<T extends N2oActionCell> extends AbstractCellElementIOv2<T>
        implements ActionsAwareIO<T> {

    @Override
    public void io(Element e, T c, IOProcessor p) {
        super.io(e, c, p);
        action(e, c, p, "input-text", "field", "date-time", "input-select");
    }

    @Override
    public Namespace actionsNamespace() {
        return ActionIOv1.NAMESPACE;
    }
}
