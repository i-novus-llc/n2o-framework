package net.n2oapp.framework.config.io.cell.v3;


import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.*;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись ячейки со списком
 */
@Component
public class ListCellElementIOv3 extends AbstractCellElementIOv3<N2oListCell> {

    @Override
    public void io(Element e, N2oListCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "color", c::getColor, c::setColor);
        p.attribute(e, "label-field-id", c::getLabelFieldId, c::setLabelFieldId);
        p.attributeInteger(e, "size", c::getSize, c::setSize);
        p.attributeBoolean(e, "inline", c::getInline, c::setInline);
        p.attribute(e, "separator", c::getSeparator, c::setSeparator);
        p.anyChild(e, null, c::getCell, c::setCell, p.oneOf(N2oCell.class)
                .add("text", N2oTextCell.class, new TextCellElementIOv3())
                .add("badge", N2oBadgeCell.class, new BadgeCellElementIOv3())
                .add("link", N2oLinkCell.class, new LinkCellElementIOv3())
                .add("cell", N2oCustomCell.class, new CustomCellElementIOv3()));
    }

    @Override
    public String getElementName() {
        return "list";
    }

    @Override
    public Class<N2oListCell> getElementClass() {
        return N2oListCell.class;
    }
}
