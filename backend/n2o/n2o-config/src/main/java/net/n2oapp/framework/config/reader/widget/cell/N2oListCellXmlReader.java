package net.n2oapp.framework.config.reader.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oListCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import net.n2oapp.framework.config.reader.widget.SwitchReader;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * @author V. Alexeev.
 */
@Component
public class N2oListCellXmlReader extends AbstractN2oCellXmlReader<N2oListCell>{
    @Override
    public String getElementName() {
        return "list";
    }

    @Override
    public N2oListCell read(Element element) {
        N2oListCell listCell = new N2oListCell();
        N2oSwitch n2oSwitch = new SwitchReader().read(element);
        listCell.setN2oSwitch(n2oSwitch);
        listCell.setLabelFieldId(ReaderJdomUtil.getAttributeString(element, "label-field-id"));
        return listCell;
    }

    @Override
    public Class<N2oListCell> getElementClass() {
        return N2oListCell.class;
    }
}
