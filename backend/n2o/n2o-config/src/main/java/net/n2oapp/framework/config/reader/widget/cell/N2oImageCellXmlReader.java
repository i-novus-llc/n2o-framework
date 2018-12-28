package net.n2oapp.framework.config.reader.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageCell;
import org.jdom.Element;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeEnum;
@Component
public class N2oImageCellXmlReader extends AbstractN2oCellXmlReader<N2oImageCell> {
    @Override
    public String getElementName() {
        return "image";
    }

    @Override
    public N2oImageCell read(Element element) {
        if (element == null)
            return null;
        N2oImageCell imageCell = new N2oImageCell();
        imageCell.setWidth(getAttributeString(element, "width"));
        imageCell.setShape(getAttributeEnum(element, "shape", ImageShape.class));
        return imageCell;
    }

    @Override
    public Class<N2oImageCell> getElementClass() {
        return N2oImageCell.class;
    }
}
