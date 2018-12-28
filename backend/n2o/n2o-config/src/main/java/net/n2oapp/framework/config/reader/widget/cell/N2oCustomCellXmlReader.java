package net.n2oapp.framework.config.reader.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCustomCell;
import net.n2oapp.framework.config.reader.tools.PropertiesReaderV1;
import net.n2oapp.framework.config.reader.widget.SwitchReader;
import org.jdom.Element;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;

/**
 * @author rgalina
 * @since 01.03.2016
 */
@Component
public class N2oCustomCellXmlReader extends AbstractN2oCellXmlReader<N2oCustomCell> {
    @Override
    public String getElementName() {
        return "custom";
    }

    @Override
    public N2oCustomCell read(Element element) {
        if (element == null)
            return null;
        N2oCustomCell custom = new N2oCustomCell();
        custom.setCssClassSwitch(new SwitchReader("css-class-field-id").read(element));
        custom.setSrc(getAttributeString(element, "src"));
        custom.setProperties(PropertiesReaderV1.getInstance().read(element, element.getNamespace()));
        return custom;
    }

    @Override
    public Class<N2oCustomCell> getElementClass() {
        return N2oCustomCell.class;
    }
}
