package net.n2oapp.framework.config.reader.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
import net.n2oapp.framework.config.reader.widget.SwitchReader;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * @author rgalina
 * @since 01.03.2016
 */
@Component
public class N2oTextCellXmlReader extends AbstractN2oCellXmlReader<N2oTextCell>  {
    @Override
    public String getElementName() {
        return "text";
    }

    @Override
    public N2oTextCell read(Element element) {
        if (element == null)
            return null;
        N2oTextCell textCell = new N2oTextCell();
        N2oSwitch styleSwitch = new SwitchReader("style-field-id").read(element);
        textCell.setClassSwitch(styleSwitch);
        return textCell;
    }

    @Override
    public Class<N2oTextCell> getElementClass() {
        return N2oTextCell.class;
    }
}
