package net.n2oapp.framework.config.io.cell.v3;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.config.io.cell.v2.SwitchIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись ячейки с текстом со стилем или без версии 3.0
 */
@Component
public class TextCellElementIOv3 extends AbstractCellElementIOv3<N2oTextCell> {
    @Override
    public void io(Element e, N2oTextCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "format", c::getFormat, c::setFormat);
        p.attribute(e, "subtext-field-id", c::getSubTextFieldKey, c::setSubTextFieldKey);
        p.attribute(e, "subtext-format", c::getSubTextFormat, c::setSubTextFormat);
        p.attribute(e, "icon", c::getIcon, c::setIcon);
        p.attributeEnum(e, "icon-position", c::getIconPosition, c::setIconPosition, PositionEnum.class);
        p.child(e, null, "switch", c::getClassSwitch, c::setClassSwitch, new SwitchIO());
    }

    @Override
    public String getElementName() {
        return "text";
    }

    @Override
    public Class<N2oTextCell> getElementClass() {
        return N2oTextCell.class;
    }
}
