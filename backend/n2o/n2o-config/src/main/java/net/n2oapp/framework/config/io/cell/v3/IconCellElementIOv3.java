package net.n2oapp.framework.config.io.cell.v3;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oIconCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.config.io.cell.v2.SwitchIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись ячейки с иконкой
 */
@Component
public class IconCellElementIOv3 extends AbstractCellElementIOv3<N2oIconCell> {

    @Override
    public void io(Element e, N2oIconCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "text", c::getText, c::setText);
        p.attribute(e, "icon", c::getIcon, c::setIcon);
        p.attributeEnum(e, "position", c::getPosition, c::setPosition, PositionEnum.class);
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
