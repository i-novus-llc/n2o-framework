package net.n2oapp.framework.config.reader.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oProgressBarCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.metadata.global.view.widget.table.Size;
import net.n2oapp.framework.config.reader.widget.SwitchReader;
import org.jdom.Element;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeBoolean;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeEnum;

/**
 * @author rgalina
 * @since 09.03.2016
 */
@Component
public class N2oProgressBarCellXmlReader extends AbstractN2oCellXmlReader<N2oProgressBarCell> {
    @Override
    public String getElementName() {
        return "progress-bar";
    }

    @Override
    public N2oProgressBarCell read(Element element) {
        if (element == null)
            return null;
        N2oProgressBarCell progressBar = new N2oProgressBarCell();
        N2oSwitch pbSwitch = new SwitchReader(null).read(element);
        progressBar.setStyleSwitch(pbSwitch);
        progressBar.setSize(getAttributeEnum(element, "size", Size.class));
        progressBar.setActive(getAttributeBoolean(element, "active"));
        progressBar.setStriped(getAttributeBoolean(element, "striped"));
        return progressBar;
    }

    @Override
    public Class<N2oProgressBarCell> getElementClass() {
        return N2oProgressBarCell.class;
    }
}
