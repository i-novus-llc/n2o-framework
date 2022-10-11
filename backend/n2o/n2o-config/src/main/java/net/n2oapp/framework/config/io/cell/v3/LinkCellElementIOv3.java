package net.n2oapp.framework.config.io.cell.v3;

import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oLinkCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись ячейки с ссылкой
 */
@Component
public class LinkCellElementIOv3 extends AbstractActionCellElementIOv3<N2oLinkCell> {

    @Override
    public void io(Element e, N2oLinkCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "icon", c::getIcon, c::setIcon);
        p.attributeEnum(e, "type", c::getType, c::setType, IconType.class);
        p.attribute(e, "url", c::getUrl, c::setUrl);
        p.attributeEnum(e, "target", c::getTarget, c::setTarget, Target.class);
    }

    @Override
    public String actionsSequenceTag() {
        return null;
    }

    @Override
    public String getElementName() {
        return "link";
    }

    @Override
    public Class<N2oLinkCell> getElementClass() {
        return N2oLinkCell.class;
    }
}
