package net.n2oapp.framework.config.io.widget.table.cell;

import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oRowClick;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.TypedElementIO;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import org.jdom.Element;
import org.springframework.stereotype.Component;


/**
 * Чтение\запись модели "клик по таблице"
 */
@Component
public class RowClickIO implements TypedElementIO<N2oRowClick> {

    @Override
    public Class<N2oRowClick> getElementClass() {
        return N2oRowClick.class;
    }

    @Override
    public String getElementName() {
        return "click";
    }

    @Override
    public void io(Element e, N2oRowClick m, IOProcessor p) {
        p.attribute(e, "action-id", m::getActionId, m::setActionId);
        p.anyChild(e, null, m::getAction, m::setAction, p.anyOf(N2oAction.class),  ActionIOv1.NAMESPACE);
    }
}
