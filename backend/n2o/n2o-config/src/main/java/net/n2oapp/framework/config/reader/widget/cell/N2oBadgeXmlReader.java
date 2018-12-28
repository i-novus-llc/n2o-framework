package net.n2oapp.framework.config.reader.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oBadgeCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.Position;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import net.n2oapp.framework.config.reader.widget.SwitchReader;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * @author V. Alexeev.
 * @date 18.03.2016
 */
@Component
public class N2oBadgeXmlReader extends AbstractN2oCellXmlReader<N2oBadgeCell> {

    @Override
    public String getElementName() {
        return "badge";
    }

    @Override
    public N2oBadgeCell read(Element element) {
        if(element == null) return null;
        String text = ReaderJdomUtil.getAttributeString(element, "text");
        Position position = ReaderJdomUtil.getAttributeEnum(element, "position", Position.class);
        N2oSwitch n2oSwitch = new SwitchReader().read(element);
        N2oBadgeCell badge = new N2oBadgeCell();
        badge.setPosition(position);
        badge.setText(text);
        badge.setN2oSwitch(n2oSwitch);
        badge.setNamespaceUri(element.getNamespaceURI());
        return badge;
    }

    @Override
    public Class<N2oBadgeCell> getElementClass() {
        return N2oBadgeCell.class;
    }
}
