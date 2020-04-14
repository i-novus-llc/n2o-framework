package net.n2oapp.framework.config.io.widget.table.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oIconCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.Position;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись ячейки с иконкой
 */
@Component
public class IconCellElementIOv2 extends AbstractCellElementIOv2<N2oIconCell> {
    private Namespace actionDefaultNamespace = ActionIOv1.NAMESPACE;

    @Override
    public void io(Element e, N2oIconCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "text", c::getText, c::setText);
        p.attribute(e, "icon", c::getIcon, c::setIcon);
        p.attributeEnum(e, "type", c::getIconType, c::setIconType, IconType.class);
        p.attributeEnum(e, "position", c::getPosition, c::setPosition, Position.class);
        p.child(e, null, "switch", c::getIconSwitch, c::setIconSwitch, new SwitchIO());
    }

    @Override
    public String getElementName() {
        return "icon";
    }

    @Override
    public Class<N2oIconCell> getElementClass() {
        return N2oIconCell.class;
    }
}
