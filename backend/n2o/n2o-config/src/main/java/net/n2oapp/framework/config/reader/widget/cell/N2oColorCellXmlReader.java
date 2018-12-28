package net.n2oapp.framework.config.reader.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oColorCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.config.reader.widget.SwitchReader;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * @author rgalina
 * @since 01.03.2016
 */
@Component
public class N2oColorCellXmlReader extends AbstractN2oCellXmlReader<N2oColorCell>  {
    @Override
    public String getElementName() {
        return "color";
    }

    @Override
    public N2oColorCell read(Element element) {
        if (element == null)
            return null;
        N2oSwitch styleSwitch = new SwitchReader("color-field-id").read(element);
        N2oColorCell textCell = new N2oColorCell(styleSwitch);
        return textCell;
    }

    @Override
    public Class<N2oColorCell> getElementClass() {
        return N2oColorCell.class;
    }
}
