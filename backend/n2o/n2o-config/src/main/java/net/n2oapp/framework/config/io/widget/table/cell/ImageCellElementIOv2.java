package net.n2oapp.framework.config.io.widget.table.cell;

import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.ImageStatusElement;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись ячейки с изображением
 */
@Component
public class ImageCellElementIOv2 extends AbstractCellElementIOv2<N2oImageCell> {
    private Namespace actionDefaultNamespace = ActionIOv1.NAMESPACE;

    @Override
    public void io(Element e, N2oImageCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "action-id", c::getActionId, c::setActionId);
        p.attribute(e, "width", c::getWidth, c::setWidth);
        p.attributeEnum(e, "shape", c::getShape, c::setShape, ImageShape.class);
        p.children(e, "statuses", "status", c::getStatuses, c::setStatuses, ImageStatusElement::new, this::statuses);
        p.anyChild(e, "action", c::getN2oAction, c::setN2oAction, p.anyOf(N2oAction.class), actionDefaultNamespace);
        p.attribute(e, "title", c::getTitle, c::setTitle);
        p.attribute(e, "description", c::getDescription, c::setDescription);
        p.attribute(e, "data", c::getData, c::setData);
        p.attributeEnum(e, "text-position", c::getTextPosition, c::setTextPosition, N2oImageCell.Position.class);
    }

    private void statuses(Element e, ImageStatusElement c, IOProcessor p) {
        p.attribute(e, "src", c::getSrc, c::setSrc);
        p.attribute(e, "field-id", c::getFieldId, c::setFieldId);
        p.attribute(e, "icon", c::getIcon, c::setIcon);
        p.attributeEnum(e, "place", c::getPlace, c::setPlace, ImageStatusElement.Place.class);
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