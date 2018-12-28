package net.n2oapp.framework.config.reader.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oIconCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.Position;
import net.n2oapp.framework.config.reader.widget.SwitchReader;
import org.jdom.Element;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeEnum;

/**
 * @author rgalina
 * @since 01.03.2016
 */
@Component
public class N2oIconCellXmlReader extends AbstractN2oCellXmlReader<N2oIconCell> {
    @Override
    public String getElementName() {
        return "icon";
    }

    @Override
    public N2oIconCell read(Element element) {
        if (element == null)
            return null;
        N2oIconCell iconCell = new N2oIconCell();
        N2oSwitch iconSwitch = new SwitchReader("icon-field-id").read(element);
        iconCell.setIconSwitch(iconSwitch);
        iconCell.setType(getAttributeEnum(element, "type", LabelType.class));
        iconCell.setPosition(getAttributeEnum(element, "position",Position.class));
        return iconCell;
    }

    @Override
    public Class<N2oIconCell> getElementClass() {
        return N2oIconCell.class;
    }
}
