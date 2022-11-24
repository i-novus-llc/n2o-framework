package net.n2oapp.framework.config.io.cell.v2;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageStatusElement;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.cell.ImageStatusElementPlace;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись ячейки с изображением
 */
@Component
public class ImageCellElementIOv2 extends AbstractActionCellElementIOv2<N2oImageCell> {

    @Override
    public void io(Element e, N2oImageCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "width", c::getWidth, c::setWidth);
        p.attributeEnum(e, "shape", c::getShape, c::setShape, ShapeType.class);
        p.children(e, "statuses", "status", c::getStatuses, c::setStatuses, N2oImageStatusElement::new, this::statuses);
        p.attribute(e, "title", c::getTitle, c::setTitle);
        p.attribute(e, "description", c::getDescription, c::setDescription);
        p.attribute(e, "data", c::getData, c::setData);
        p.attributeEnum(e, "text-position", c::getTextPosition, c::setTextPosition, N2oImageCell.Position.class);
    }

    private void statuses(Element e, N2oImageStatusElement c, IOProcessor p) {
        p.attribute(e, "src", c::getSrc, c::setSrc);
        p.attribute(e, "field-id", c::getFieldId, c::setFieldId);
        p.attribute(e, "icon", c::getIcon, c::setIcon);
        p.attributeEnum(e, "place", c::getPlace, c::setPlace, ImageStatusElementPlace.class);
    }

    @Override
    public String actionsSequenceTag() {
        return "action";
    }

    @Override
    public String getElementName() {
        return "image";
    }

    @Override
    public Class<N2oImageCell> getElementClass() {
        return N2oImageCell.class;
    }
}
