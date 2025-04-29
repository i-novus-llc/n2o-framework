package net.n2oapp.framework.config.io.cell.v3;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oBadgeCell;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.cell.v2.SwitchIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись ячейки со значком
 */
@Component
public class BadgeCellElementIOv3 extends AbstractCellElementIOv3<N2oBadgeCell> {
    @Override
    public void io(Element e, N2oBadgeCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attributeEnum(e, "position", c::getPosition, c::setPosition, PositionEnum.class);
        p.attribute(e,"text",c::getText,c::setText);
        p.attribute(e,"text-format",c::getTextFormat,c::setTextFormat);
        p.attribute(e,"color",c::getColor,c::setColor);
        p.attribute(e,"format",c::getFormat,c::setFormat);
        p.attribute(e,"image-field-id", c::getImageFieldId, c::setImageFieldId);
        p.attributeEnum(e, "image-position", c::getImagePosition, c::setImagePosition, PositionEnum.class);
        p.attributeEnum(e, "image-shape", c::getImageShape, c::setImageShape, ShapeTypeEnum.class);
        p.attributeEnum(e, "shape", c::getShape, c::setShape, ShapeTypeEnum.class);
        p.child(e, null, "switch", c::getN2oSwitch, c::setN2oSwitch, new SwitchIO());
    }


    @Override
    public String getElementName() {
        return "badge";
    }

    @Override
    public Class<N2oBadgeCell> getElementClass() {
        return N2oBadgeCell.class;
    }
}
