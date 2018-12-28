package net.n2oapp.framework.config.io.widget.table.cell;

import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oLink;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 *Чтение\запись ячейки со ссылкой
 */
@Component
public class LinkCellElementIOv2 extends AbstractCellElementIOv2<N2oLink> {
    private Namespace actionDefaultNamespace = ActionIOv1.NAMESPACE;

    @Override
    public void io(Element e, N2oLink c, IOProcessor p) {
        super.io(e,c,p);
        p.attribute(e, "action-id", c::getActionId, c::setActionId);
        p.attribute(e, "icon", c::getIcon, c::setIcon);
        p.attributeEnum(e, "type", c::getType, c::setType, IconType.class);
        p.anyChild(e, null, c::getAction, c::setAction, p.anyOf(N2oAction.class), actionDefaultNamespace);
    }

    @Override
    public String getElementName() {
        return "link" ;
    }

    @Override
    public Class<N2oLink> getElementClass() {
        return N2oLink.class;
    }
}
